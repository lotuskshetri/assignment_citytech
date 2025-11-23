// filepath: src/hooks/useMerchants.ts

import { useState, useEffect, useCallback } from 'react';
import { merchantService } from '../services/merchantService';
import { MerchantStats, MerchantFilters } from '../types/merchant.types';

interface UseMerchantsResult {
  merchants: MerchantStats[];
  loading: boolean;
  error: string | null;
  pagination: {
    total: number;
    limit: number;
    offset: number;
    currentPage: number;
    totalPages: number;
    hasNext: boolean;
    hasPrevious: boolean;
  };
  refetch: (filters?: MerchantFilters) => void;
}

export const useMerchants = (initialFilters: MerchantFilters = {}): UseMerchantsResult => {
  const [merchants, setMerchants] = useState<MerchantStats[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [pagination, setPagination] = useState({
    total: 0,
    limit: 10,
    offset: 0,
    currentPage: 1,
    totalPages: 0,
    hasNext: false,
    hasPrevious: false,
  });

  const fetchMerchants = useCallback(async (filters: MerchantFilters = {}) => {
    try {
      setLoading(true);
      setError(null);

      console.log('Fetching merchants with filters:', { ...initialFilters, ...filters });

      const response = await merchantService.getMerchants({
        ...initialFilters,
        ...filters,
      });

      console.log('Received response:', response);

      setMerchants(response.merchants);
      setPagination(response.pagination);
    } catch (err: any) {
      console.error('Error fetching merchants - Full error:', err);
      console.error('Error response:', err.response);
      console.error('Error message:', err.message);
      setError(err.response?.data?.message || err.message || 'Failed to fetch merchants');
    } finally {
      setLoading(false);
    }
  }, [initialFilters]);

  useEffect(() => {
    fetchMerchants();
  }, [fetchMerchants]);

  return {
    merchants,
    loading,
    error,
    pagination,
    refetch: fetchMerchants,
  };
};

