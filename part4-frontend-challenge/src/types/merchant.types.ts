// filepath: src/types/merchant.types.ts

export interface MerchantStats {
  merchantId: string;
  merchantName: string;
  totalTransactions: number;
  totalRevenue: number;
  completedCount: number;
  failedCount: number;
  pendingCount: number;
  successRate: number;
  lastTransactionDate: string; // "2025-11-18"
  firstTransactionDate: string; // "2025-11-16"
  status: string;
  averageTransactionAmount: number;
}

export interface MerchantListResponse {
  merchants: MerchantStats[];
  pagination: {
    total: number;
    limit: number;
    offset: number;
    currentPage: number;
    totalPages: number;
    hasNext: boolean;
    hasPrevious: boolean;
  };
}

export interface MerchantFilters {
  search?: string;
  limit?: number;
  offset?: number;
}

export interface MerchantDetails extends MerchantStats {
  // Add additional fields if needed for detailed view
  email?: string;
  phone?: string;
  address?: string;
}

