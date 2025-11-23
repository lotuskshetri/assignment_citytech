package com.payment.service;

import com.payment.dto.revenue.*;
import java.time.LocalDate;

/**
 * Service for revenue reports and analysis.
 */
public interface RevenueService {

    /**
     * Get revenue breakdown by time period (daily/weekly/monthly).
     */
    RevenueByPeriodResponse getRevenueByPeriod(LocalDate startDate, LocalDate endDate, String period);

    /**
     * Get revenue breakdown by merchant.
     */
    RevenueByMerchantResponse getRevenueByMerchant(LocalDate startDate, LocalDate endDate, Integer limit);

    /**
     * Forecast revenue based on historical trends.
     */
    RevenueForecastResponse forecastRevenue(int periods);

    /**
     * Analyze year-over-year growth.
     */
    GrowthAnalysisResponse analyzeGrowth(int currentYear, int comparisonYear);

    /**
     * Get top performing merchants.
     */
    TopPerformersResponse getTopPerformers(LocalDate startDate, LocalDate endDate, Integer limit, String sortBy);
}

