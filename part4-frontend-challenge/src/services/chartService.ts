// Chart Data API Service

import { apiClient } from './api';

export interface ChartDataResponse {
  labels: string[];
  datasets: Array<{
    label: string;
    data: number[];
    backgroundColor?: string | string[];
    borderColor?: string | string[];
    borderWidth?: number;
    fill?: boolean;
  }>;
  chartType: string;
}

interface ChartParams {
  startDate: string;
  endDate: string;
}

export const chartService = {
  /**
   * Get line chart data for trends
   */
  async getLineChartData(
    params: ChartParams & { metric?: string; groupBy?: string }
  ): Promise<ChartDataResponse> {
    const response = await apiClient.get<ChartDataResponse>(
      '/charts/line/trends',
      { params }
    );
    return response.data;
  },

  /**
   * Get bar chart data for comparisons
   */
  async getBarChartData(
    params: ChartParams & { compareBy?: string }
  ): Promise<ChartDataResponse> {
    const response = await apiClient.get<ChartDataResponse>(
      '/charts/bar/comparison',
      { params }
    );
    return response.data;
  },

  /**
   * Get pie chart data for distributions
   */
  async getPieChartData(
    params: ChartParams & { distributeBy?: string }
  ): Promise<ChartDataResponse> {
    const response = await apiClient.get<ChartDataResponse>(
      '/charts/pie/distribution',
      { params }
    );
    return response.data;
  },

  /**
   * Get recent transactions for real-time updates
   */
  async getRecentTransactions(since?: string, limit: number = 50): Promise<any[]> {
    const params: any = { limit };
    if (since) {
      params.since = since;
    }
    const response = await apiClient.get<any[]>('/charts/data/recent', { params });
    return response.data;
  },

  /**
   * Get drill-down data for specific category
   */
  async getDrillDownData(
    category: string,
    categoryValue: string,
    params: ChartParams
  ): Promise<ChartDataResponse> {
    const response = await apiClient.get<ChartDataResponse>(
      `/charts/drill-down/${category}`,
      { params: { ...params, categoryValue } }
    );
    return response.data;
  },
};

