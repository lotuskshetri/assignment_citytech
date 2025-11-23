package com.payment.service;

import com.payment.dto.chart.ChartDataResponse;
import com.payment.dto.chart.ChartDataset;
import com.payment.entity.TransactionMaster;
import com.payment.repository.TransactionRepository;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class ChartDataServiceImpl implements ChartDataService {

    private final TransactionRepository transactionRepository;

    public ChartDataServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public ChartDataResponse getLineChartData(String metric, LocalDate startDate, LocalDate endDate, String groupBy) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        List<TransactionMaster> transactions = transactionRepository.findByDateRangeForAnalytics(sqlStartDate, sqlEndDate);

        // Group by period
        Map<String, List<TransactionMaster>> grouped = new LinkedHashMap<>();
        for (TransactionMaster txn : transactions) {
            String period = formatPeriod(txn.getTxnDate(), groupBy);
            grouped.computeIfAbsent(period, k -> new ArrayList<>()).add(txn);
        }

        List<String> labels = new ArrayList<>(grouped.keySet());
        List<Object> data;
        String datasetLabel;

        switch (metric.toLowerCase()) {
            case "volume":
            case "count":
                data = grouped.values().stream()
                    .map(txns -> (Object) (long) txns.size())
                    .collect(Collectors.toList());
                datasetLabel = "Transaction Volume";
                break;
            case "avgamount":
            case "average":
                data = grouped.values().stream()
                    .map(txns -> {
                        BigDecimal total = txns.stream().map(TransactionMaster::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                        return (Object) (txns.isEmpty() ? BigDecimal.ZERO : total.divide(BigDecimal.valueOf(txns.size()), 2, RoundingMode.HALF_UP));
                    })
                    .collect(Collectors.toList());
                datasetLabel = "Average Transaction Amount";
                break;
            default: // revenue
                data = grouped.values().stream()
                    .map(txns -> (Object) txns.stream().map(TransactionMaster::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                    .collect(Collectors.toList());
                datasetLabel = "Revenue";
        }

        ChartDataset dataset = new ChartDataset(datasetLabel, data);
        dataset.setBorderColor("rgb(75, 192, 192)");
        dataset.setBackgroundColor("rgba(75, 192, 192, 0.2)");
        dataset.setBorderWidth(2);
        dataset.setFill(true);

        ChartDataResponse response = new ChartDataResponse();
        response.setLabels(labels);
        response.setDatasets(Arrays.asList(dataset));
        response.setChartType("line");

        return response;
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
    public ChartDataResponse getBarChartData(String compareBy, LocalDate startDate, LocalDate endDate) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        List<TransactionMaster> transactions = transactionRepository.findByDateRangeForAnalytics(sqlStartDate, sqlEndDate);

        List<String> labels;
        List<Object> revenueData;
        List<Object> countData;

        switch (compareBy.toLowerCase()) {
            case "cardtype":
            case "card":
                Map<String, List<TransactionMaster>> byCard = transactions.stream()
                    .collect(Collectors.groupingBy(TransactionMaster::getCardType));
                labels = new ArrayList<>(byCard.keySet());
                revenueData = byCard.values().stream()
                    .map(txns -> (Object) txns.stream().map(TransactionMaster::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                    .collect(Collectors.toList());
                countData = byCard.values().stream()
                    .map(txns -> (Object) (long) txns.size())
                    .collect(Collectors.toList());
                break;

            case "status":
                Map<String, List<TransactionMaster>> byStatus = transactions.stream()
                    .collect(Collectors.groupingBy(TransactionMaster::getStatus));
                labels = new ArrayList<>(byStatus.keySet());
                countData = byStatus.values().stream()
                    .map(txns -> (Object) (long) txns.size())
                    .collect(Collectors.toList());
                revenueData = new ArrayList<>();
                break;

            default: // merchant
                Map<String, List<TransactionMaster>> byMerchant = transactions.stream()
                    .collect(Collectors.groupingBy(TransactionMaster::getMerchantId));

                List<Map.Entry<String, List<TransactionMaster>>> sorted = byMerchant.entrySet().stream()
                    .sorted((a, b) -> {
                        BigDecimal revA = a.getValue().stream().map(TransactionMaster::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal revB = b.getValue().stream().map(TransactionMaster::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                        return revB.compareTo(revA);
                    })
                    .limit(10)
                    .collect(Collectors.toList());

                labels = sorted.stream().map(Map.Entry::getKey).collect(Collectors.toList());
                revenueData = sorted.stream()
                    .map(e -> (Object) e.getValue().stream().map(TransactionMaster::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                    .collect(Collectors.toList());
                countData = sorted.stream()
                    .map(e -> (Object) (long) e.getValue().size())
                    .collect(Collectors.toList());
        }

        ChartDataResponse response = new ChartDataResponse();
        response.setLabels(labels);

        List<ChartDataset> datasets = new ArrayList<>();
        if (!revenueData.isEmpty()) {
            ChartDataset revenueDataset = new ChartDataset("Revenue", revenueData);
            revenueDataset.setBackgroundColor("rgba(54, 162, 235, 0.8)");
            revenueDataset.setBorderColor("rgb(54, 162, 235)");
            revenueDataset.setBorderWidth(1);
            datasets.add(revenueDataset);
        }

        ChartDataset countDataset = new ChartDataset("Transaction Count", countData);
        countDataset.setBackgroundColor("rgba(255, 99, 132, 0.8)");
        countDataset.setBorderColor("rgb(255, 99, 132)");
        countDataset.setBorderWidth(1);
        datasets.add(countDataset);

        response.setDatasets(datasets);
        response.setChartType("bar");

        return response;
    }

    @Override
    public ChartDataResponse getPieChartData(String distributeBy, LocalDate startDate, LocalDate endDate) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        List<TransactionMaster> transactions = transactionRepository.findByDateRangeForAnalytics(sqlStartDate, sqlEndDate);

        List<String> labels;
        List<Object> data;

        switch (distributeBy.toLowerCase()) {
            case "cardtype":
            case "card":
                Map<String, Long> byCard = transactions.stream()
                    .collect(Collectors.groupingBy(TransactionMaster::getCardType, Collectors.counting()));
                labels = new ArrayList<>(byCard.keySet());
                data = new ArrayList<>(byCard.values());
                break;

            case "merchant":
                Map<String, BigDecimal> byMerchant = transactions.stream()
                    .collect(Collectors.groupingBy(TransactionMaster::getMerchantId,
                        Collectors.reducing(BigDecimal.ZERO, TransactionMaster::getAmount, BigDecimal::add)));

                List<Map.Entry<String, BigDecimal>> sorted = byMerchant.entrySet().stream()
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                    .limit(5)
                    .collect(Collectors.toList());

                labels = sorted.stream().map(Map.Entry::getKey).collect(Collectors.toList());
                data = sorted.stream().map(Map.Entry::getValue).collect(Collectors.toList());
                break;

            default: // status
                Map<String, Long> byStatus = transactions.stream()
                    .collect(Collectors.groupingBy(TransactionMaster::getStatus, Collectors.counting()));
                labels = new ArrayList<>(byStatus.keySet());
                data = new ArrayList<>(byStatus.values());
        }

        String[] colors = {"rgba(255, 99, 132, 0.8)", "rgba(54, 162, 235, 0.8)",
                          "rgba(255, 206, 86, 0.8)", "rgba(75, 192, 192, 0.8)",
                          "rgba(153, 102, 255, 0.8)"};

        ChartDataset dataset = new ChartDataset("Distribution", data);
        dataset.setBackgroundColor(Arrays.toString(colors));

        ChartDataResponse response = new ChartDataResponse();
        response.setLabels(labels);
        response.setDatasets(Arrays.asList(dataset));
        response.setChartType("pie");

        return response;
    }

    @Override
    public List<TransactionMaster> getRecentTransactions(Instant since, int limit) {
        return transactionRepository.findRecentTransactions(since, limit);
    }

    @Override
    public ChartDataResponse getDrillDownData(String category, String categoryValue, LocalDate startDate, LocalDate endDate) {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        // For drill-down, we'll show daily breakdown for the specific category
        List<TransactionMaster> transactions;

        if ("merchant".equalsIgnoreCase(category)) {
            transactions = transactionRepository.findByMerchantIdAndDateRange(categoryValue, sqlStartDate, sqlEndDate);
        } else {
            transactions = transactionRepository.findByDateRangeForAnalytics(sqlStartDate, sqlEndDate);
            // Filter by category value
            final String finalCategoryValue = categoryValue;
            transactions = transactions.stream()
                .filter(txn -> {
                    if ("cardtype".equalsIgnoreCase(category)) {
                        return finalCategoryValue.equals(txn.getCardType());
                    } else if ("status".equalsIgnoreCase(category)) {
                        return finalCategoryValue.equals(txn.getStatus());
                    }
                    return true;
                })
                .collect(Collectors.toList());
        }

        // Group by day
        Map<String, List<TransactionMaster>> dailyGroups = new LinkedHashMap<>();
        for (TransactionMaster txn : transactions) {
            String day = txn.getTxnDate().toLocalDate().toString();
            dailyGroups.computeIfAbsent(day, k -> new ArrayList<>()).add(txn);
        }

        List<String> labels = new ArrayList<>(dailyGroups.keySet());
        List<Object> revenueData = dailyGroups.values().stream()
            .map(txns -> (Object) txns.stream().map(TransactionMaster::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
            .collect(Collectors.toList());
        List<Object> countData = dailyGroups.values().stream()
            .map(txns -> (Object) (long) txns.size())
            .collect(Collectors.toList());

        ChartDataset revenueDataset = new ChartDataset("Revenue", revenueData);
        revenueDataset.setBorderColor("rgb(54, 162, 235)");
        revenueDataset.setBackgroundColor("rgba(54, 162, 235, 0.2)");
        revenueDataset.setBorderWidth(2);

        ChartDataset countDataset = new ChartDataset("Count", countData);
        countDataset.setBorderColor("rgb(255, 99, 132)");
        countDataset.setBackgroundColor("rgba(255, 99, 132, 0.2)");
        countDataset.setBorderWidth(2);

        ChartDataResponse response = new ChartDataResponse();
        response.setLabels(labels);
        response.setDatasets(Arrays.asList(revenueDataset, countDataset));
        response.setChartType("line");

        return response;
    }
}

