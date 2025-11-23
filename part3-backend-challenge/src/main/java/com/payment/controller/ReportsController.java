package com.payment.controller;

import com.payment.dto.revenue.*;
import com.payment.service.RevenueService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Controller for revenue reports and analysis endpoints.
 */
@Controller("/api/v1/reports")
@Tag(name = "Reports")
public class ReportsController {

    private final RevenueService revenueService;

    public ReportsController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @Get("/revenue/by-period")
    @Operation(
        summary = "Get revenue by time period",
        description = "Returns revenue breakdown by daily, weekly, or monthly periods"
    )
    public HttpResponse<RevenueByPeriodResponse> getRevenueByPeriod(
            @QueryValue Optional<LocalDate> startDate,
            @QueryValue Optional<LocalDate> endDate,
            @QueryValue(defaultValue = "daily") String period
    ) {
        LocalDate start = startDate.orElse(LocalDate.now().minusDays(30));
        LocalDate end = endDate.orElse(LocalDate.now());

        RevenueByPeriodResponse response = revenueService.getRevenueByPeriod(start, end, period);
        return HttpResponse.ok(response);
    }

    @Get("/revenue/by-merchant")
    @Operation(
        summary = "Get revenue by merchant",
        description = "Returns revenue breakdown by merchant with rankings"
    )
    public HttpResponse<RevenueByMerchantResponse> getRevenueByMerchant(
            @QueryValue Optional<LocalDate> startDate,
            @QueryValue Optional<LocalDate> endDate,
            @QueryValue(defaultValue = "10") Integer limit
    ) {
        LocalDate start = startDate.orElse(LocalDate.now().minusDays(30));
        LocalDate end = endDate.orElse(LocalDate.now());

        RevenueByMerchantResponse response = revenueService.getRevenueByMerchant(start, end, limit);
        return HttpResponse.ok(response);
    }

    @Get("/revenue/forecast")
    @Operation(
        summary = "Get revenue forecast",
        description = "Returns predicted revenue based on historical trends"
    )
    public HttpResponse<RevenueForecastResponse> getForecast(
            @QueryValue(defaultValue = "7") int periods
    ) {
        RevenueForecastResponse response = revenueService.forecastRevenue(periods);
        return HttpResponse.ok(response);
    }

    @Get("/revenue/growth")
    @Operation(
        summary = "Get year-over-year growth analysis",
        description = "Returns year-over-year revenue comparison and growth rates"
    )
    public HttpResponse<GrowthAnalysisResponse> analyzeGrowth(
            @QueryValue Optional<Integer> currentYear,
            @QueryValue Optional<Integer> comparisonYear
    ) {
        int current = currentYear.orElse(LocalDate.now().getYear());
        int comparison = comparisonYear.orElse(current - 1);

        GrowthAnalysisResponse response = revenueService.analyzeGrowth(current, comparison);
        return HttpResponse.ok(response);
    }

    @Get("/merchants/top-performers")
    @Operation(
        summary = "Get top performing merchants",
        description = "Returns ranked list of top merchants by revenue or transaction volume"
    )
    public HttpResponse<TopPerformersResponse> getTopPerformers(
            @QueryValue Optional<LocalDate> startDate,
            @QueryValue Optional<LocalDate> endDate,
            @QueryValue(defaultValue = "10") Integer limit,
            @QueryValue(defaultValue = "revenue") String sortBy
    ) {
        LocalDate start = startDate.orElse(LocalDate.now().minusDays(30));
        LocalDate end = endDate.orElse(LocalDate.now());

        TopPerformersResponse response = revenueService.getTopPerformers(start, end, limit, sortBy);
        return HttpResponse.ok(response);
    }
}

