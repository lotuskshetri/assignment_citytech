package com.payment.controller;

import com.payment.dto.analytics.*;
import com.payment.service.AnalyticsService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Controller for transaction analytics endpoints.
 */
@Controller("/api/v1/analytics")
@Tag(name = "Analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Get("/transactions/volume")
    @Operation(
        summary = "Get transaction volume analytics",
        description = "Returns transaction volume grouped by day, week, or month"
    )
    public HttpResponse<VolumeAnalyticsResponse> getTransactionVolume(
            @QueryValue Optional<LocalDate> startDate,
            @QueryValue Optional<LocalDate> endDate,
            @QueryValue(defaultValue = "day") String groupBy
    ) {
        LocalDate start = startDate.orElse(LocalDate.now().minusDays(30));
        LocalDate end = endDate.orElse(LocalDate.now());

        VolumeAnalyticsResponse response = analyticsService.getTransactionVolume(start, end, groupBy);
        return HttpResponse.ok(response);
    }

    @Get("/transactions/success-rate")
    @Operation(
        summary = "Get success rate analysis",
        description = "Returns transaction success vs failure rates with status breakdown"
    )
    public HttpResponse<SuccessRateResponse> getSuccessRate(
            @QueryValue Optional<LocalDate> startDate,
            @QueryValue Optional<LocalDate> endDate
    ) {
        LocalDate start = startDate.orElse(LocalDate.now().minusDays(30));
        LocalDate end = endDate.orElse(LocalDate.now());

        SuccessRateResponse response = analyticsService.getSuccessRate(start, end);
        return HttpResponse.ok(response);
    }

    @Get("/transactions/trends")
    @Operation(
        summary = "Get transaction amount trends",
        description = "Returns average transaction amount trends over time"
    )
    public HttpResponse<TrendsResponse> getTransactionTrends(
            @QueryValue Optional<LocalDate> startDate,
            @QueryValue Optional<LocalDate> endDate
    ) {
        LocalDate start = startDate.orElse(LocalDate.now().minusDays(30));
        LocalDate end = endDate.orElse(LocalDate.now());

        TrendsResponse response = analyticsService.getTransactionTrends(start, end);
        return HttpResponse.ok(response);
    }

    @Get("/transactions/peak-times")
    @Operation(
        summary = "Get peak transaction times",
        description = "Returns heatmap data showing transaction volume by hour and day of week"
    )
    public HttpResponse<PeakTimesResponse> getPeakTimes(
            @QueryValue Optional<LocalDate> startDate,
            @QueryValue Optional<LocalDate> endDate
    ) {
        LocalDate start = startDate.orElse(LocalDate.now().minusDays(30));
        LocalDate end = endDate.orElse(LocalDate.now());

        PeakTimesResponse response = analyticsService.getPeakTimes(start, end);
        return HttpResponse.ok(response);
    }

    @Get("/transactions/card-distribution")
    @Operation(
        summary = "Get card type distribution",
        description = "Returns distribution of transactions by card type"
    )
    public HttpResponse<CardDistributionResponse> getCardDistribution(
            @QueryValue Optional<LocalDate> startDate,
            @QueryValue Optional<LocalDate> endDate
    ) {
        LocalDate start = startDate.orElse(LocalDate.now().minusDays(30));
        LocalDate end = endDate.orElse(LocalDate.now());

        CardDistributionResponse response = analyticsService.getCardDistribution(start, end);
        return HttpResponse.ok(response);
    }
}

