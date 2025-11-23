package com.payment.service;

import com.payment.dto.chart.ChartDataResponse;
import com.payment.entity.TransactionMaster;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for generating chart data in frontend-compatible formats.
 */
public interface ChartDataService {

    /**
     * Get line chart data for trends over time.
     */
    ChartDataResponse getLineChartData(String metric, LocalDate startDate, LocalDate endDate, String groupBy);

    /**
     * Get bar chart data for comparisons.
     */
    ChartDataResponse getBarChartData(String compareBy, LocalDate startDate, LocalDate endDate);

    /**
     * Get pie chart data for distribution.
     */
    ChartDataResponse getPieChartData(String distributeBy, LocalDate startDate, LocalDate endDate);

    /**
     * Get recent transactions for real-time updates.
     */
    List<TransactionMaster> getRecentTransactions(Instant since, int limit);

    /**
     * Get drill-down data for a specific category.
     */
    ChartDataResponse getDrillDownData(String category, String categoryValue, LocalDate startDate, LocalDate endDate);
}

