package com.payment.service;

import com.payment.dto.revenue.*;
import com.payment.entity.TransactionMaster;
import com.payment.repository.TransactionRepository;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class RevenueServiceImpl implements RevenueService {

    private final TransactionRepository transactionRepository;

    public RevenueServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public RevenueByPeriodResponse getRevenueByPeriod(LocalDate startDate, LocalDate endDate, String period) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        List<TransactionMaster> transactions = transactionRepository.findByDateRangeForAnalytics(sqlStartDate, sqlEndDate);

        // Group by period
        Map<String, List<TransactionMaster>> grouped = new LinkedHashMap<>();
        for (TransactionMaster txn : transactions) {
            String periodKey = formatPeriod(txn.getTxnDate(), period);
            grouped.computeIfAbsent(periodKey, k -> new ArrayList<>()).add(txn);
        }

        List<PeriodRevenue> periods = grouped.entrySet().stream()
            .map(entry -> {
                List<TransactionMaster> txns = entry.getValue();
                BigDecimal revenue = txns.stream().map(TransactionMaster::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                long count = txns.size();
                BigDecimal avg = count > 0 ? revenue.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                return new PeriodRevenue(entry.getKey(), revenue, count, avg);
            })
            .collect(Collectors.toList());

        RevenueByPeriodResponse response = new RevenueByPeriodResponse();
        response.setPeriods(periods);
        response.setGroupBy(period);

        // Calculate totals
        BigDecimal totalRevenue = periods.stream().map(PeriodRevenue::getRevenue).reduce(BigDecimal.ZERO, BigDecimal::add);
        Long totalTransactions = periods.stream().map(PeriodRevenue::getTransactionCount).reduce(0L, Long::sum);

        response.setTotalRevenue(totalRevenue);
        response.setTotalTransactions(totalTransactions);

        return response;
    }

    private String formatPeriod(java.sql.Date date, String period) {
        LocalDate localDate = date.toLocalDate();
        switch (period.toLowerCase()) {
            case "weekly":
            case "week":
                return localDate.with(java.time.DayOfWeek.MONDAY).toString();
            case "monthly":
            case "month":
                return localDate.withDayOfMonth(1).toString();
            default:
                return localDate.toString();
        }
    }

    @Override
    public RevenueByMerchantResponse getRevenueByMerchant(LocalDate startDate, LocalDate endDate, Integer limit) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        if (limit == null || limit <= 0) {
            limit = 10;
        }

        List<TransactionMaster> transactions = transactionRepository.findByDateRangeForAnalytics(sqlStartDate, sqlEndDate);

        // Group by merchant
        Map<String, List<TransactionMaster>> byMerchant = transactions.stream()
            .collect(Collectors.groupingBy(TransactionMaster::getMerchantId));

        // Calculate revenue for each merchant
        List<MerchantRevenueData> merchants = byMerchant.entrySet().stream()
            .map(entry -> {
                List<TransactionMaster> txns = entry.getValue();
                BigDecimal revenue = txns.stream().map(TransactionMaster::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                long count = txns.size();
                BigDecimal avg = count > 0 ? revenue.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

                MerchantRevenueData data = new MerchantRevenueData();
                data.setMerchantId(entry.getKey());
                data.setRevenue(revenue);
                data.setTransactionCount(count);
                data.setAverageTransaction(avg);
                return data;
            })
            .sorted(Comparator.comparing(MerchantRevenueData::getRevenue).reversed())
            .limit(limit)
            .collect(Collectors.toList());

        // Calculate total and add rankings and percentages
        BigDecimal totalRevenue = merchants.stream().map(MerchantRevenueData::getRevenue).reduce(BigDecimal.ZERO, BigDecimal::add);

        int rank = 1;
        for (MerchantRevenueData data : merchants) {
            data.setRank(rank++);
            if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal percentage = data.getRevenue().divide(totalRevenue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
                data.setPercentageOfTotal(percentage.doubleValue());
            } else {
                data.setPercentageOfTotal(0.0);
            }
        }

        RevenueByMerchantResponse response = new RevenueByMerchantResponse();
        response.setMerchants(merchants);
        response.setTotalRevenue(totalRevenue);
        response.setTotalMerchants(merchants.size());

        return response;
    }

    @Override
    public RevenueForecastResponse forecastRevenue(int periods) {
        // Get last 30 days of data for forecasting
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);

        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        List<TransactionMaster> transactions = transactionRepository.findByDateRangeForAnalytics(sqlStartDate, sqlEndDate);

        // Group by day
        Map<String, List<TransactionMaster>> dailyGroups = new LinkedHashMap<>();
        for (TransactionMaster txn : transactions) {
            String day = txn.getTxnDate().toLocalDate().toString();
            dailyGroups.computeIfAbsent(day, k -> new ArrayList<>()).add(txn);
        }

        List<PeriodRevenue> historicalData = dailyGroups.entrySet().stream()
            .map(entry -> {
                List<TransactionMaster> txns = entry.getValue();
                BigDecimal revenue = txns.stream().map(TransactionMaster::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                long count = txns.size();
                BigDecimal avg = count > 0 ? revenue.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                return new PeriodRevenue(entry.getKey(), revenue, count, avg);
            })
            .collect(Collectors.toList());

        // Simple moving average forecast
        List<ForecastDataPoint> forecast = calculateMovingAverageForecast(historicalData, periods);

        RevenueForecastResponse response = new RevenueForecastResponse();
        response.setHistoricalData(historicalData);
        response.setForecast(forecast);
        response.setMethod("moving_average");
        response.setConfidence(0.75); // 75% confidence for simple forecast

        return response;
    }

    @Override
    public GrowthAnalysisResponse analyzeGrowth(int currentYear, int comparisonYear) {
        List<TransactionMaster> transactions = transactionRepository.findByYears(currentYear, comparisonYear);

        Map<Integer, Map<Integer, BigDecimal>> yearMonthRevenue = new HashMap<>();

        for (TransactionMaster txn : transactions) {
            LocalDate date = txn.getTxnDate().toLocalDate();
            int year = date.getYear();
            int month = date.getMonthValue();
            BigDecimal revenue = txn.getAmount();

            yearMonthRevenue.computeIfAbsent(year, k -> new HashMap<>())
                .merge(month, revenue, BigDecimal::add);
        }

        List<MonthlyComparison> monthlyComparisons = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            BigDecimal currentYearRevenue = yearMonthRevenue.getOrDefault(currentYear, new HashMap<>()).getOrDefault(month, BigDecimal.ZERO);
            BigDecimal previousYearRevenue = yearMonthRevenue.getOrDefault(comparisonYear, new HashMap<>()).getOrDefault(month, BigDecimal.ZERO);

            MonthlyComparison comparison = new MonthlyComparison();
            comparison.setMonth(month);
            comparison.setMonthName(Month.of(month).name());
            comparison.setCurrentYearRevenue(currentYearRevenue);
            comparison.setPreviousYearRevenue(previousYearRevenue);

            // Calculate growth rate
            if (previousYearRevenue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal growth = currentYearRevenue.subtract(previousYearRevenue)
                    .divide(previousYearRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
                comparison.setGrowthRate(growth.doubleValue());
            } else {
                comparison.setGrowthRate(0.0);
            }

            monthlyComparisons.add(comparison);
        }

        BigDecimal currentYearTotal = monthlyComparisons.stream()
            .map(MonthlyComparison::getCurrentYearRevenue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal comparisonYearTotal = monthlyComparisons.stream()
            .map(MonthlyComparison::getPreviousYearRevenue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        double overallGrowthRate = 0.0;
        if (comparisonYearTotal.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal growth = currentYearTotal.subtract(comparisonYearTotal)
                .divide(comparisonYearTotal, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
            overallGrowthRate = growth.doubleValue();
        }

        GrowthAnalysisResponse response = new GrowthAnalysisResponse();
        response.setCurrentYear(currentYear);
        response.setComparisonYear(comparisonYear);
        response.setOverallGrowthRate(overallGrowthRate);
        response.setCurrentYearTotal(currentYearTotal);
        response.setComparisonYearTotal(comparisonYearTotal);
        response.setMonthlyComparison(monthlyComparisons);

        return response;
    }

    @Override
    public TopPerformersResponse getTopPerformers(LocalDate startDate, LocalDate endDate, Integer limit, String sortBy) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        if (limit == null || limit <= 0) {
            limit = 10;
        }

        List<TransactionMaster> transactions = transactionRepository.findByDateRangeForAnalytics(sqlStartDate, sqlEndDate);

        // Group by merchant
        Map<String, List<TransactionMaster>> byMerchant = transactions.stream()
            .collect(Collectors.groupingBy(TransactionMaster::getMerchantId));

        // Calculate revenue for each merchant
        List<MerchantRevenueData> topMerchants = byMerchant.entrySet().stream()
            .map(entry -> {
                List<TransactionMaster> txns = entry.getValue();
                BigDecimal revenue = txns.stream().map(TransactionMaster::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                long count = txns.size();
                BigDecimal avg = count > 0 ? revenue.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

                MerchantRevenueData data = new MerchantRevenueData();
                data.setMerchantId(entry.getKey());
                data.setRevenue(revenue);
                data.setTransactionCount(count);
                data.setAverageTransaction(avg);
                return data;
            })
            .sorted(Comparator.comparing(MerchantRevenueData::getRevenue).reversed())
            .limit(limit)
            .collect(Collectors.toList());

        BigDecimal totalRevenue = topMerchants.stream().map(MerchantRevenueData::getRevenue).reduce(BigDecimal.ZERO, BigDecimal::add);

        int rank = 1;
        for (MerchantRevenueData data : topMerchants) {
            data.setRank(rank++);
            if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal percentage = data.getRevenue().divide(totalRevenue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
                data.setPercentageOfTotal(percentage.doubleValue());
            } else {
                data.setPercentageOfTotal(0.0);
            }
        }

        TopPerformersResponse response = new TopPerformersResponse();
        response.setTopMerchants(topMerchants);
        response.setSortedBy(sortBy);
        response.setDateRange(startDate + " to " + endDate);

        return response;
    }

    /**
     * Calculate simple moving average forecast.
     */
    private List<ForecastDataPoint> calculateMovingAverageForecast(List<PeriodRevenue> historical, int periods) {
        if (historical.isEmpty()) {
            return new ArrayList<>();
        }

        // Use last 7 days for moving average
        int window = Math.min(7, historical.size());
        List<BigDecimal> lastRevenues = historical.stream()
            .skip(Math.max(0, historical.size() - window))
            .map(PeriodRevenue::getRevenue)
            .collect(Collectors.toList());

        BigDecimal avgRevenue = lastRevenues.stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(lastRevenues.size()), 2, RoundingMode.HALF_UP);

        // Calculate standard deviation for confidence bounds
        BigDecimal variance = BigDecimal.ZERO;
        for (BigDecimal rev : lastRevenues) {
            BigDecimal diff = rev.subtract(avgRevenue);
            variance = variance.add(diff.multiply(diff));
        }
        BigDecimal stdDev = BigDecimal.valueOf(Math.sqrt(
            variance.divide(BigDecimal.valueOf(lastRevenues.size()), 2, RoundingMode.HALF_UP).doubleValue()
        ));

        List<ForecastDataPoint> forecast = new ArrayList<>();
        LocalDate lastDate = LocalDate.parse(historical.get(historical.size() - 1).getPeriod());

        for (int i = 1; i <= periods; i++) {
            LocalDate forecastDate = lastDate.plusDays(i);
            ForecastDataPoint point = new ForecastDataPoint(forecastDate.toString(), avgRevenue);

            // Add confidence bounds (±1 std deviation)
            point.setLowerBound(avgRevenue.subtract(stdDev).max(BigDecimal.ZERO));
            point.setUpperBound(avgRevenue.add(stdDev));

            forecast.add(point);
        }

        return forecast;
    }
}

