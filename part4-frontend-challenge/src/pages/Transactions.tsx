import { useState } from 'react';
import { FilterState, DEFAULT_FILTERS } from '../types/transaction';
import { TransactionList } from '../components/common/TransactionList';
import { TransactionSummary } from '../components/common/TransactionSummary';
import { TransactionFilters } from '../components/transactions/TransactionFilters';
import { Pagination } from '../components/transactions/Pagination';
import { useTransactions } from '../hooks/useTransactions';

/**
 * Transactions Page Component
 * Displays transaction dashboard with summary, filters, and pagination
 */
export const Transactions = () => {
  const [filters, setFilters] = useState<FilterState>({
    ...DEFAULT_FILTERS,
    merchantId: '', // Start with empty to show all transactions
  });

  // Don't fallback - if merchantId is empty, pass null to fetch all transactions
  const merchantId = filters.merchantId && filters.merchantId.trim() !== ''
    ? filters.merchantId
    : null;

  const { data, loading, error } = useTransactions(merchantId as any, filters);

  const handleFilterChange = (newFilters: Partial<FilterState>) => {
    setFilters(prev => ({
      ...prev,
      ...newFilters,
      page: 0, // Reset to first page when filters change
    }));
  };

  const handlePageChange = (page: number) => {
    setFilters(prev => ({ ...prev, page }));
  };

  const handlePageSizeChange = (size: number) => {
    setFilters(prev => ({ ...prev, size, page: 0 }));
  };

  const totalPages = data ? Math.ceil(data.totalTransactions / filters.size) : 0;

  return (
    <main className="container">
      <h1>💳 All Transactions</h1>
      <p className="subtitle">
        {merchantId
          ? `Filtered by Merchant: ${merchantId}`
          : 'Showing all merchants'}
        {filters.status && ` • Status: ${filters.status}`}
        {(filters.startDate || filters.endDate) && ` • Date: ${filters.startDate || 'earliest'} to ${filters.endDate || 'latest'}`}
      </p>

      {/* Transaction Filters */}
      <div className="filters-section">
        <TransactionFilters
          onFilterChange={handleFilterChange}
          currentFilters={{
            status: filters.status,
            startDate: filters.startDate,
            endDate: filters.endDate,
            merchantId: filters.merchantId,
          }}
        />
      </div>

      {error && (
        <div className="error-message" style={{ padding: '1rem', background: '#fee2e2', borderRadius: '8px', color: '#991b1b', margin: '1rem 0' }}>
          Error loading transactions: {error.message}
        </div>
      )}

      {loading && !data && (
        <div className="loading-message" style={{ padding: '2rem', textAlign: 'center', color: '#64748b' }}>
          Loading transactions...
        </div>
      )}

      {data && (
        <>
          <div className="summary-section">
            <TransactionSummary 
              transactions={data.transactions || []}
              totalTransactions={data.totalTransactions || 0}
              summary={data.summary}
            />
          </div>

          <div className="transactions-section">
            <TransactionList 
              transactions={data.transactions || []} 
              loading={loading}
            />
          </div>

          {/* Pagination */}
          <div className="pagination-section">
            <Pagination
              currentPage={filters.page}
              totalPages={totalPages}
              totalItems={data.totalTransactions}
              itemsPerPage={filters.size}
              onPageChange={handlePageChange}
              onPageSizeChange={handlePageSizeChange}
            />
          </div>
        </>
      )}
    </main>
  );
};
