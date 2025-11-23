// Custom Hook for Analytics Data

import { useState, useEffect, useCallback } from 'react';
import { analyticsService } from '../services/analyticsService';
import {
  TransactionVolume,
  SuccessRate,
  TransactionTrends,
  PeakTimesHeatmap,
  CardDistribution,
  DateRangeParams,
} from '../types/analytics.types';

interface UseAnalyticsResult {
  volume: TransactionVolume | null;
  successRate: SuccessRate | null;
  trends: TransactionTrends | null;
  peakTimes: PeakTimesHeatmap | null;
  cardDistribution: CardDistribution | null;
  loading: boolean;
  error: string | null;
  refetch: () => void;
}

export const useAnalytics = (dateRange: DateRangeParams): UseAnalyticsResult => {
  const [volume, setVolume] = useState<TransactionVolume | null>(null);
  const [successRate, setSuccessRate] = useState<SuccessRate | null>(null);
  const [trends, setTrends] = useState<TransactionTrends | null>(null);
  const [peakTimes, setPeakTimes] = useState<PeakTimesHeatmap | null>(null);
  const [cardDistribution, setCardDistribution] = useState<CardDistribution | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchAnalytics = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);

      console.log('Fetching analytics with date range:', dateRange);

      // Fetch all analytics data in parallel
      const [volumeData, successData, trendsData, peakData, cardData] = await Promise.all([
        analyticsService.getTransactionVolume(dateRange),
        analyticsService.getSuccessRate(dateRange),
        analyticsService.getTransactionTrends(dateRange),
        analyticsService.getPeakTimesHeatmap(dateRange),
        analyticsService.getCardDistribution(dateRange),
      ]);

      setVolume(volumeData);
      setSuccessRate(successData);
      setTrends(trendsData);
      setPeakTimes(peakData);
      setCardDistribution(cardData);

      console.log('Analytics data loaded successfully');
    } catch (err: any) {
      console.error('Error fetching analytics:', err);
      setError(err.response?.data?.message || err.message || 'Failed to fetch analytics');
    } finally {
      setLoading(false);
    }
  }, [dateRange.startDate, dateRange.endDate]);

  useEffect(() => {
    fetchAnalytics();
  }, [fetchAnalytics]);

  return {
    volume,
    successRate,
    trends,
    peakTimes,
    cardDistribution,
    loading,
    error,
    refetch: fetchAnalytics,
  };
};

