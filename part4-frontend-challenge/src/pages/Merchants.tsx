import React, { useState } from 'react';
import { useMerchants } from '../hooks/useMerchants';
import { MerchantTable } from '../components/merchants/MerchantTable';
import { MerchantFilters } from '../components/merchants/MerchantFilters';
import { Pagination } from '../components/common/Pagination';
import './Merchants.css';

export const Merchants: React.FC = () => {
  const [filters, setFilters] = useState({
    limit: 10,
    offset: 0,
    search: '',
  });

  const { merchants, loading, error, pagination, refetch } = useMerchants(filters);

  const handleSearchChange = (search: string) => {
    setFilters((prev) => ({
      ...prev,
      search,
      offset: 0, // Reset to first page on search
    }));
    refetch({ ...filters, search, offset: 0 });
  };

  const handlePageChange = (newOffset: number) => {
    setFilters((prev) => ({ ...prev, offset: newOffset }));
    refetch({ ...filters, offset: newOffset });
  };

  const handleRefresh = () => {
    refetch(filters);
  };

  const handleMerchantClick = (merchantId: string) => {
    console.log('Merchant clicked:', merchantId);
    window.location.href = `/merchants/${merchantId}`;
  };

  const handleAddMerchant = () => {
    window.location.href = '/merchants/new';
  };

  return (
    <div className="merchants-page">
      <div className="page-header">
        <div className="header-content">
          <div>
            <h1>🏢 Merchants Management</h1>
            <p className="page-description">
              View and manage merchant accounts with transaction statistics
            </p>
          </div>
          <button onClick={handleAddMerchant} className="btn-add-merchant">
            ➕ Add New Merchant
          </button>
        </div>
      </div>

      {error && (
        <div className="error-banner">
          <span className="error-icon">⚠</span>
          <span>{error}</span>
          <button onClick={handleRefresh} className="retry-btn">
            Retry
          </button>
        </div>
      )}

      <MerchantFilters
        onFilterChange={handleSearchChange}
        onRefresh={handleRefresh}
      />

      <div className="merchants-stats">
        <div className="stat-card">
          <div className="stat-label">Total Merchants</div>
          <div className="stat-value">{pagination.total}</div>
        </div>
        <div className="stat-card">
          <div className="stat-label">Showing</div>
          <div className="stat-value">{merchants.length}</div>
        </div>
      </div>

      <MerchantTable
        merchants={merchants}
        loading={loading}
        onMerchantClick={handleMerchantClick}
      />

      {!loading && merchants.length > 0 && (
        <Pagination
          total={pagination.total}
          limit={pagination.limit}
          offset={pagination.offset}
          onPageChange={handlePageChange}
        />
      )}
    </div>
  );
};
