// Analytics API Service

import { apiClient } from './api';
import {
  TransactionVolume,
  SuccessRate,
  TransactionTrends,
  PeakTimesHeatmap,
  CardDistribution,
  DateRangeParams,
} from '../types/analytics.types';

export const analyticsService = {
  /**
   * Get transaction volume analytics
   */
  async getTransactionVolume(params: DateRangeParams): Promise<TransactionVolume> {
    const response = await apiClient.get<TransactionVolume>(
      `/analytics/transactions/volume`,
      { params }
    );
    return response.data;
  },

  /**
   * Get success rate analysis
   */
  async getSuccessRate(params: DateRangeParams): Promise<SuccessRate> {
    const response = await apiClient.get<SuccessRate>(
      `/analytics/transactions/success-rate`,
      { params }
    );
    return response.data;
  },

  /**
   * Get transaction trends over time
   */
  async getTransactionTrends(params: DateRangeParams): Promise<TransactionTrends> {
    const response = await apiClient.get<TransactionTrends>(
      `/analytics/transactions/trends`,
      { params }
    );
    return response.data;
  },

  /**
   * Get peak times heatmap data
   */
  async getPeakTimesHeatmap(params: DateRangeParams): Promise<PeakTimesHeatmap> {
    const response = await apiClient.get<PeakTimesHeatmap>(
      `/analytics/transactions/peak-times`,
      { params }
    );
    return response.data;
  },

  /**
   * Get card type distribution
   */
  async getCardDistribution(params: DateRangeParams): Promise<CardDistribution> {
    const response = await apiClient.get<CardDistribution>(
      `/analytics/transactions/card-distribution`,
      { params }
    );
    return response.data;
  },
};

