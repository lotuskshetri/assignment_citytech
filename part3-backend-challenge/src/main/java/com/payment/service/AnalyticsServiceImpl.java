package com.payment.service;


import com.payment.dto.analytics.*;
import com.payment.entity.TransactionMaster;
import com.payment.repository.TransactionRepository;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class AnalyticsServiceImpl implements AnalyticsService {

    private final TransactionRepository transactionRepository;

    public AnalyticsServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public VolumeAnalyticsResponse getTransactionVolume(LocalDate startDate, LocalDate endDate, String groupBy) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        List<TransactionMaster> transactions = transactionRepository.findByDateRangeForAnalytics(sqlStartDate, sqlEndDate);

        // Group transactions by period
        Map<String, List<TransactionMaster>> groupedByPeriod = new LinkedHashMap<>();

        for (TransactionMaster txn : transactions) {
            String period = formatPeriod(txn.getTxnDate(), groupBy);
            groupedByPeriod.computeIfAbsent(period, k -> new ArrayList<>()).add(txn);
        }

        // Calculate volume data for each period
        List<VolumeDataPoint> dataPoints = groupedByPeriod.entrySet().stream()
            .map(entry -> {
                String period = entry.getKey();
                List<TransactionMaster> txns = entry.getValue();

                long count = txns.size();
                BigDecimal total = txns.stream()
                    .map(TransactionMaster::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal average = count > 0 ? total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

                return new VolumeDataPoint(period, count, total, average);
            })
            .collect(Collectors.toList());

        String dateRange = startDate + " to " + endDate;
        return new VolumeAnalyticsResponse(dataPoints, groupBy, dateRange);
    }

    private String formatPeriod(java.sql.Date date, String groupBy) {
        LocalDate localDate = date.toLocalDate();
        switch (groupBy.toLowerCase()) {
            case "week":
                return localDate.with(java.time.DayOfWeek.MONDAY).toString();
            case "month":
                return localDate.withDayOfMonth(1).toString();
            default:
                return localDate.toString();
        }
    }

    @Override
    public SuccessRateResponse getSuccessRate(LocalDate startDate, LocalDate endDate) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        List<TransactionMaster> transactions = transactionRepository.findByDateRangeForAnalytics(sqlStartDate, sqlEndDate);

        SuccessRateResponse response = new SuccessRateResponse();
        Map<String, Long> breakdown = new HashMap<>();

        long total = transactions.size();
        long completed = 0;
        long pending = 0;
        long failed = 0;
        long reversed = 0;

        for (TransactionMaster txn : transactions) {
            String status = txn.getStatus();
            breakdown.merge(status, 1L, Long::sum);

            switch (status.toLowerCase()) {
                case "completed":
                    completed++;
                    break;
                case "pending":
                    pending++;
                    break;
                case "failed":
                    failed++;
                    break;
                case "reversed":
                    reversed++;
                    break;
            }
        }

        response.setTotalTransactions(total);
        response.setCompletedCount(completed);
        response.setPendingCount(pending);
        response.setFailedCount(failed);
        response.setReversedCount(reversed);
        response.setStatusBreakdown(breakdown);

        // Calculate success rate
        if (total > 0) {
            double successRate = (completed * 100.0) / total;
            response.setSuccessRate(Math.round(successRate * 100.0) / 100.0);
        } else {
            response.setSuccessRate(0.0);
        }

        return response;
    }

    @Override
    public TrendsResponse getTransactionTrends(LocalDate startDate, LocalDate endDate) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        List<TransactionMaster> transactions = transactionRepository.findByDateRangeForAnalytics(sqlStartDate, sqlEndDate);

        // Group by day
        Map<String, List<TransactionMaster>> dailyGroups = new LinkedHashMap<>();
        for (TransactionMaster txn : transactions) {
            String day = txn.getTxnDate().toLocalDate().toString();
            dailyGroups.computeIfAbsent(day, k -> new ArrayList<>()).add(txn);
        }

        List<TrendDataPoint> trends = new ArrayList<>();
        BigDecimal previousAvg = null;

        for (Map.Entry<String, List<TransactionMaster>> entry : dailyGroups.entrySet()) {
            List<TransactionMaster> dayTxns = entry.getValue();
            BigDecimal total = dayTxns.stream().map(TransactionMaster::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal avgAmount = dayTxns.isEmpty() ? BigDecimal.ZERO : total.divide(BigDecimal.valueOf(dayTxns.size()), 2, RoundingMode.HALF_UP);

            TrendDataPoint point = new TrendDataPoint(entry.getKey(), avgAmount, (long) dayTxns.size());

            // Calculate change percentage from previous period
            if (previousAvg != null && previousAvg.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal change = avgAmount.subtract(previousAvg);
                BigDecimal changePercent = change.divide(previousAvg, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
                point.setChangePercentage(changePercent.doubleValue());
            }

            trends.add(point);
            previousAvg = avgAmount;
        }

        TrendsResponse response = new TrendsResponse(trends);

        // Determine overall trend direction
        if (!trends.isEmpty() && trends.size() > 1) {
            BigDecimal firstAvg = trends.get(0).getAverageAmount();
            BigDecimal lastAvg = trends.get(trends.size() - 1).getAverageAmount();

            if (lastAvg.compareTo(firstAvg) > 0) {
                response.setTrendDirection("up");
                BigDecimal change = lastAvg.subtract(firstAvg)
                    .divide(firstAvg, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
                response.setOverallChangePercentage(change.doubleValue());
            } else if (lastAvg.compareTo(firstAvg) < 0) {
                response.setTrendDirection("down");
                BigDecimal change = lastAvg.subtract(firstAvg)
                    .divide(firstAvg, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
                response.setOverallChangePercentage(change.doubleValue());
            } else {
                response.setTrendDirection("stable");
                response.setOverallChangePercentage(0.0);
            }
        }

        return response;
    }

    @Override
    public PeakTimesResponse getPeakTimes(LocalDate startDate, LocalDate endDate) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        List<TransactionMaster> transactions = transactionRepository.findByDateRangeForAnalytics(sqlStartDate, sqlEndDate);

        // Group by hour and day of week (all in UTC)
        Map<String, Long> timeCounts = new HashMap<>();
        for (TransactionMaster txn : transactions) {
            if (txn.getLocalTxnDateTime() != null) {
                // Convert Instant to LocalDateTime in UTC
                java.time.LocalDateTime dateTime = java.time.LocalDateTime.ofInstant(
                    txn.getLocalTxnDateTime(),
                    java.time.ZoneOffset.UTC
                );
                int hour = dateTime.getHour();
                int dayOfWeek = dateTime.getDayOfWeek().getValue() % 7; // Convert to 0-6 (Sunday=0)
                String key = hour + "-" + dayOfWeek;
                timeCounts.merge(key, 1L, Long::sum);
            }
        }

        List<HeatmapCell> heatmapData = timeCounts.entrySet().stream()
            .map(entry -> {
                String[] parts = entry.getKey().split("-");
                return new HeatmapCell(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), entry.getValue());
            })
            .collect(Collectors.toList());

        PeakTimesResponse response = new PeakTimesResponse(heatmapData);

        // Find peak hour and day
        if (!heatmapData.isEmpty()) {
            HeatmapCell peak = heatmapData.stream()
                .max(Comparator.comparing(HeatmapCell::getTransactionCount))
                .orElse(null);

            if (peak != null) {
                response.setBusiestHour(peak.getHour());
                response.setBusiestDay(peak.getDayOfWeek());
                response.setPeakTransactionCount(peak.getTransactionCount());
            }
        }

        return response;
    }

    @Override
    public CardDistributionResponse getCardDistribution(LocalDate startDate, LocalDate endDate) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        // Fetch all transactions and group by card type in Java
        List<TransactionMaster> transactions = transactionRepository.findByDateRangeForAnalytics(sqlStartDate, sqlEndDate);

        // Group by card type
        Map<String, CardTypeData> cardTypeMap = new HashMap<>();
        long totalTransactions = 0;

        for (TransactionMaster txn : transactions) {
            String cardType = txn.getCardType();
            CardTypeData data = cardTypeMap.computeIfAbsent(cardType, k -> new CardTypeData(k, 0L, BigDecimal.ZERO));

            data.setCount(data.getCount() + 1);
            data.setTotalAmount(data.getTotalAmount().add(txn.getAmount()));
            totalTransactions++;
        }

        List<CardTypeData> distribution = new ArrayList<>(cardTypeMap.values());

        // Calculate percentages
        final long total = totalTransactions;
        distribution.forEach(data -> {
            if (total > 0) {
                double percentage = (data.getCount() * 100.0) / total;
                data.setPercentage(Math.round(percentage * 100.0) / 100.0);
            } else {
                data.setPercentage(0.0);
            }
        });

        return new CardDistributionResponse(distribution, totalTransactions);
    }
}

