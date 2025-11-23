// filepath: src/services/merchantService.ts

import { apiClient } from './api';
import { MerchantListResponse, MerchantFilters, MerchantStats, MerchantDetails } from '../types/merchant.types';

export const merchantService = {
  /**
   * Fetch paginated list of merchants with statistics
   */
  async getMerchants(filters: MerchantFilters = {}): Promise<MerchantListResponse> {
    try {
      const params = new URLSearchParams();

      // Only add parameters if they have values
      if (filters.limit !== undefined && filters.limit !== null) {
        params.append('limit', filters.limit.toString());
      }
      if (filters.offset !== undefined && filters.offset !== null) {
        params.append('offset', filters.offset.toString());
      }
      if (filters.search && filters.search.trim() !== '') {
        params.append('search', filters.search);
      }

      const url = params.toString() ? `/merchants?${params.toString()}` : '/merchants';
      const fullUrl = `${apiClient.defaults.baseURL}${url}`;

      console.log('=== API Call Details ===');
      console.log('Base URL:', apiClient.defaults.baseURL);
      console.log('Endpoint:', url);
      console.log('Full URL:', fullUrl);
      console.log('Filters:', filters);

      const response = await apiClient.get<MerchantListResponse>(url);

      console.log('=== API Response ===');
      console.log('Status:', response.status);
      console.log('Data:', response.data);

      return response.data;
    } catch (error: any) {
      console.error('=== API Error ===');
      console.error('Error message:', error.message);
      console.error('Error code:', error.code);
      console.error('Response:', error.response);
      console.error('Full error:', error);
      throw error;
    }
  },

  /**
   * Get single merchant summary
   */
  async getMerchantById(id: string): Promise<MerchantStats> {
    const response = await apiClient.get<MerchantStats>(`/merchants/${id}`);
    return response.data;
  },

  /**
   * Get full merchant details
   */
  async getMerchantDetails(id: string): Promise<MerchantDetails> {
    const response = await apiClient.get<MerchantDetails>(`/merchants/${id}/details`);
    return response.data;
  },

  /**
   * Create new merchant
   */
  async createMerchant(merchantData: Partial<MerchantStats>): Promise<MerchantStats> {
    const response = await apiClient.post<MerchantStats>('/merchants', merchantData);
    return response.data;
  },

  /**
   * Update existing merchant
   */
  async updateMerchant(id: string, merchantData: Partial<MerchantStats>): Promise<MerchantStats> {
    const response = await apiClient.put<MerchantStats>(`/merchants/${id}`, merchantData);
    return response.data;
  },

  /**
   * Get full merchant profile details (not just statistics)
   */
  async getMerchantFullDetails(id: string): Promise<any> {
    const response = await apiClient.get<any>(`/merchants/${id}/details`);
    return response.data;
  },
};

