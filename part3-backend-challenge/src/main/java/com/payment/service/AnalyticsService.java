package com.payment.service;

import com.payment.dto.analytics.*;
import java.time.LocalDate;

/**
 * Service for transaction analytics operations.
 */
public interface AnalyticsService {

    /**
     * Get transaction volume analytics grouped by period.
     */
    VolumeAnalyticsResponse getTransactionVolume(LocalDate startDate, LocalDate endDate, String groupBy);

    /**
     * Get success rate and status distribution.
     */
    SuccessRateResponse getSuccessRate(LocalDate startDate, LocalDate endDate);

    /**
     * Get transaction amount trends over time.
     */
    TrendsResponse getTransactionTrends(LocalDate startDate, LocalDate endDate);

    /**
     * Get peak transaction times heatmap data.
     */
    PeakTimesResponse getPeakTimes(LocalDate startDate, LocalDate endDate);

    /**
     * Get card type distribution.
     */
    CardDistributionResponse getCardDistribution(LocalDate startDate, LocalDate endDate);
}

