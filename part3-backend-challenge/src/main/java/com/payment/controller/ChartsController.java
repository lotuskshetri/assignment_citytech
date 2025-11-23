package com.payment.controller;

import com.payment.dto.chart.ChartDataResponse;
import com.payment.entity.TransactionMaster;
import com.payment.service.ChartDataService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller for Chart Data endpoints.
 * Provides Chart.js compatible data for various visualizations.
 */
@Controller("/api/v1/charts")
@Tag(name = "Chart Data", description = "Chart.js compatible data endpoints")
public class ChartsController {

    private final ChartDataService chartDataService;

    public ChartsController(ChartDataService chartDataService) {
        this.chartDataService = chartDataService;
    }

    /**
     * Get line chart data for trend visualization.
     *
     * @param metric Metric to display: revenue, volume, or average
     * @param startDate Start date (YYYY-MM-DD)
     * @param endDate End date (YYYY-MM-DD)
     * @param groupBy Grouping: day, week, or month
     * @return Chart.js compatible line chart data
     */
    @Get("/line/trends")
    @Operation(summary = "Get line chart data", description = "Returns Chart.js compatible line chart data for trends")
    public ChartDataResponse getLineChartData(
            @QueryValue(defaultValue = "revenue") String metric,
            @QueryValue Optional<LocalDate> startDate,
            @QueryValue Optional<LocalDate> endDate,
            @QueryValue(defaultValue = "day") String groupBy) {

        LocalDate start = startDate.orElse(LocalDate.now().minusDays(30));
        LocalDate end = endDate.orElse(LocalDate.now());

        return chartDataService.getLineChartData(metric, start, end, groupBy);
    }

    /**
     * Get bar chart data for comparison visualization.
     *
     * @param compareBy What to compare: merchant, cardtype, or status
     * @param startDate Start date (YYYY-MM-DD)
     * @param endDate End date (YYYY-MM-DD)
     * @return Chart.js compatible bar chart data
     */
    @Get("/bar/comparison")
    @Operation(summary = "Get bar chart data", description = "Returns Chart.js compatible bar chart data for comparisons")
    public ChartDataResponse getBarChartData(
            @QueryValue(defaultValue = "merchant") String compareBy,
            @QueryValue Optional<LocalDate> startDate,
            @QueryValue Optional<LocalDate> endDate) {

        LocalDate start = startDate.orElse(LocalDate.now().minusDays(30));
        LocalDate end = endDate.orElse(LocalDate.now());

        return chartDataService.getBarChartData(compareBy, start, end);
    }

    /**
     * Get pie chart data for distribution visualization.
     *
     * @param distributeBy What to distribute: status, cardtype, or merchant
     * @param startDate Start date (YYYY-MM-DD)
     * @param endDate End date (YYYY-MM-DD)
     * @return Chart.js compatible pie chart data
     */
    @Get("/pie/distribution")
    @Operation(summary = "Get pie chart data", description = "Returns Chart.js compatible pie chart data for distributions")
    public ChartDataResponse getPieChartData(
            @QueryValue(defaultValue = "status") String distributeBy,
            @QueryValue Optional<LocalDate> startDate,
            @QueryValue Optional<LocalDate> endDate) {

        LocalDate start = startDate.orElse(LocalDate.now().minusDays(30));
        LocalDate end = endDate.orElse(LocalDate.now());

        return chartDataService.getPieChartData(distributeBy, start, end);
    }

    /**
     * Get recent transactions for real-time updates.
     *
     * @param since Get transactions after this timestamp (ISO 8601)
     * @param limit Maximum number of transactions to return
     * @return List of recent transactions
     */
    @Get("/data/recent")
    @Operation(summary = "Get recent transactions", description = "Returns recent transactions for real-time updates")
    public List<TransactionMaster> getRecentTransactions(
            @QueryValue Optional<Instant> since,
            @QueryValue(defaultValue = "50") int limit) {

        Instant sinceTime = since.orElse(Instant.now().minusSeconds(300)); // Default: last 5 minutes

        // Limit to max 100
        if (limit > 100) {
            limit = 100;
        }

        return chartDataService.getRecentTransactions(sinceTime, limit);
    }

    /**
     * Get drill-down data for a specific category.
     *
     * @param category Category to drill into: merchant, cardtype, or status
     * @param categoryValue Specific value to filter by
     * @param startDate Start date (YYYY-MM-DD)
     * @param endDate End date (YYYY-MM-DD)
     * @return Chart.js compatible drill-down chart data
     */
    @Get("/drill-down/{category}")
    @Operation(summary = "Get drill-down data", description = "Returns detailed breakdown for a specific category")
    public ChartDataResponse getDrillDownData(
            @PathVariable String category,
            @QueryValue String categoryValue,
            @QueryValue Optional<LocalDate> startDate,
            @QueryValue Optional<LocalDate> endDate) {

        LocalDate start = startDate.orElse(LocalDate.now().minusDays(30));
        LocalDate end = endDate.orElse(LocalDate.now());

        return chartDataService.getDrillDownData(category, categoryValue, start, end);
    }
}

