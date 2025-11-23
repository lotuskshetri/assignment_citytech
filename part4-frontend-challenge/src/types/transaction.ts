// Transaction-related types and interfaces

export interface Transaction {
  txnId: number;
  amount: number;
  currency: string;
  status: 'completed' | 'pending' | 'failed' | 'reversed';
  timestamp: string;
  cardType: string;
  cardLast4: string;
  acquirer: string;
  issuer: string;
  details: TransactionDetail[];
}

export interface TransactionDetail {
  detailId: number;
  type: 'fee' | 'tax' | 'adjustment' | 'refund';
  amount: number;
  description: string;
}

export interface TransactionSummary {
  totalTransactions: number;
  totalAmount: number;
  currency: string;
  byStatus: {
    completed: number;
    pending: number;
    failed: number;
  };
}

export interface TransactionResponseSummary {
  totalCount: number;
  totalAmount: number;
  completedCount: number;
  pendingCount: number;
  failedCount: number;
}

export interface TransactionResponse {
  transactions: Transaction[];
  totalTransactions: number;
  page: number;
  size: number;
  totalPages: number;
  summary?: TransactionResponseSummary; // Summary statistics across all filtered transactions
}

export interface PaginationInfo {
  page: number;
  size: number;
  totalPages: number;
  totalElements: number;
}

export interface FilterState {
  page: number;
  size: number;
  startDate: string;
  endDate: string;
  status?: string;
  searchQuery?: string;
  merchantId?: string;
}

export const DEFAULT_FILTERS: FilterState = {
  page: 0,
  size: 20,
  startDate: '2025-11-16',
  endDate: '2025-11-18',
  status: undefined,
  searchQuery: undefined,
  merchantId: undefined,
};
