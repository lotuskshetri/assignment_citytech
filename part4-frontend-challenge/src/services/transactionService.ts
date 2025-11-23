import { get } from './api';
import { TransactionResponse, FilterState } from '../types/transaction';

/**
 * Transaction Service
 * Handles all transaction-related API calls
 */

const MERCHANT_BASE = '/merchants';

/**
 * Get transactions for a specific merchant
 * 
 * TODO: Implement this method to call the backend API
 * 
 * @param merchantId - The merchant ID
 * @param filters - Filter parameters (page, size, dates, status)
 * @returns Promise with transaction response data
 */
export const getTransactions = async (
  merchantId: string | null | undefined,
  filters: FilterState
): Promise<TransactionResponse> => {
  // Use the new transaction-focused endpoint: GET /api/v1/transactions
  // This endpoint supports filtering by merchantId, status, and date range with pagination

  const params: any = {
    page: filters.page,
    size: filters.size,
    startDate: filters.startDate,
    endDate: filters.endDate,
  };

  // Add optional filters
  if (merchantId && merchantId.trim() !== '') {
    params.merchantId = merchantId;
  }

  if (filters.status) {
    params.status = filters.status;
  }

  // Use the new transactions endpoint that supports all filters
  const url = '/transactions';

  console.log('Fetching transactions from:', url, 'with params:', params);

  try {
    const response = await get<TransactionResponse>(url, { params });
    console.log('Transactions response:', {
      total: response.totalTransactions,
      page: response.page,
      count: response.transactions?.length
    });

    return response;
  } catch (error: any) {
    console.error('Error fetching transactions:', error);
    console.error('Error response:', error.response?.data);
    throw error;
  }
};

/**
 * Get a single transaction by ID
 * (Optional - for future enhancement)
 */
export const getTransactionById = async (
  txnId: number
): Promise<any> => {
  // TODO: Implement if needed
  throw new Error('Not implemented');
};

export default {
  getTransactions,
  getTransactionById,
};
