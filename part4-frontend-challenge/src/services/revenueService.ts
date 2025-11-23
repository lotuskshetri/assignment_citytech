// Revenue API Service

import { apiClient } from './api';
import {
  RevenueByPeriodResponse,
  RevenueByMerchantResponse,
  RevenueForecastResponse,
  GrowthAnalysisResponse,
  TopPerformersResponse,
} from '../types/revenue.types';

interface DateRangeParams {
  startDate: string;
  endDate: string;
}

export const revenueService = {
  /**
   * Get revenue by time period (daily/weekly/monthly)
   */
  async getRevenueByPeriod(
    params: DateRangeParams & { period?: string }
  ): Promise<RevenueByPeriodResponse> {
    const response = await apiClient.get<RevenueByPeriodResponse>(
      '/reports/revenue/by-period',
      { params }
    );
    return response.data;
  },

  /**
   * Get revenue breakdown by merchant
   */
  async getRevenueByMerchant(
    params: DateRangeParams & { limit?: number }
  ): Promise<RevenueByMerchantResponse> {
    const response = await apiClient.get<RevenueByMerchantResponse>(
      '/reports/revenue/by-merchant',
      { params }
    );
    return response.data;
  },

  /**
   * Get revenue forecast
   */
  async getRevenueForecast(periods: number = 7): Promise<RevenueForecastResponse> {
    const response = await apiClient.get<RevenueForecastResponse>(
      '/reports/revenue/forecast',
      { params: { periods } }
    );
    return response.data;
  },

  /**
   * Get year-over-year growth analysis
   */
  async getGrowthAnalysis(
    currentYear?: number,
    comparisonYear?: number
  ): Promise<GrowthAnalysisResponse> {
    const response = await apiClient.get<GrowthAnalysisResponse>(
      '/reports/revenue/growth',
      { params: { currentYear, comparisonYear } }
    );
    return response.data;
  },

  /**
   * Get top performing merchants
   */
  async getTopPerformers(
    params: DateRangeParams & { limit?: number; sortBy?: string }
  ): Promise<TopPerformersResponse> {
    const response = await apiClient.get<TopPerformersResponse>(
      '/reports/merchants/top-performers',
      { params }
    );
    return response.data;
  },
};

