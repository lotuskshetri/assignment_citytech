// Transaction Filters Component

import React, { useState } from 'react';
import './TransactionFilters.css';

interface TransactionFiltersProps {
  onFilterChange: (filters: {
    status?: string;
    startDate?: string;
    endDate?: string;
    merchantId?: string;
  }) => void;
  currentFilters: {
    status?: string;
    startDate?: string;
    endDate?: string;
    merchantId?: string;
  };
}

export const TransactionFilters: React.FC<TransactionFiltersProps> = ({
  onFilterChange,
  currentFilters,
}) => {
  const [localFilters, setLocalFilters] = useState(currentFilters);

  const handleFilterChange = (key: string, value: string) => {
    const newFilters = { ...localFilters, [key]: value || undefined };
    setLocalFilters(newFilters);
  };

  const handleApply = () => {
    onFilterChange(localFilters);
  };

  const handleReset = () => {
    const resetFilters = {
      status: undefined,
      startDate: undefined,
      endDate: undefined,
      merchantId: currentFilters.merchantId, // Keep merchant ID
    };
    setLocalFilters(resetFilters);
    onFilterChange(resetFilters);
  };

  const getDefaultDates = () => {
    const end = new Date();
    const start = new Date();
    start.setDate(end.getDate() - 30);
    return {
      start: start.toISOString().split('T')[0],
      end: end.toISOString().split('T')[0],
    };
  };

  const defaultDates = getDefaultDates();

  return (
    <div className="transaction-filters">
      <div className="filters-header">
        <h3>🔍 Filters</h3>
      </div>

      <div className="filters-grid">
        {/* Merchant ID Filter */}
        <div className="filter-group">
          <label htmlFor="merchantId">Merchant ID</label>
          <input
            id="merchantId"
            type="text"
            placeholder="e.g., MCH-00001"
            value={localFilters.merchantId || ''}
            onChange={(e) => handleFilterChange('merchantId', e.target.value)}
            className="filter-input"
          />
          <small className="filter-hint">Leave empty for all merchants</small>
        </div>

        {/* Status Filter */}
        <div className="filter-group">
          <label htmlFor="status">Status</label>
          <select
            id="status"
            value={localFilters.status || ''}
            onChange={(e) => handleFilterChange('status', e.target.value)}
            className="filter-select"
          >
            <option value="">All Statuses</option>
            <option value="completed">Completed</option>
            <option value="pending">Pending</option>
            <option value="failed">Failed</option>
            <option value="reversed">Reversed</option>
          </select>
        </div>

        {/* Start Date Filter */}
        <div className="filter-group">
          <label htmlFor="startDate">Start Date</label>
          <input
            id="startDate"
            type="date"
            value={localFilters.startDate || defaultDates.start}
            onChange={(e) => handleFilterChange('startDate', e.target.value)}
            className="filter-input"
          />
        </div>

        {/* End Date Filter */}
        <div className="filter-group">
          <label htmlFor="endDate">End Date</label>
          <input
            id="endDate"
            type="date"
            value={localFilters.endDate || defaultDates.end}
            onChange={(e) => handleFilterChange('endDate', e.target.value)}
            className="filter-input"
            max={new Date().toISOString().split('T')[0]}
          />
        </div>
      </div>

      <div className="filters-actions">
        <button onClick={handleApply} className="btn-apply">
          Apply Filters
        </button>
        <button onClick={handleReset} className="btn-reset">
          Reset
        </button>
      </div>

      {/* Active Filters Display */}
      {(localFilters.status || localFilters.startDate || localFilters.endDate) && (
        <div className="active-filters">
          <span className="active-label">Active Filters:</span>
          {localFilters.status && (
            <span className="filter-tag">
              Status: {localFilters.status}
              <button onClick={() => handleFilterChange('status', '')}>×</button>
            </span>
          )}
          {localFilters.startDate && (
            <span className="filter-tag">
              From: {localFilters.startDate}
              <button onClick={() => handleFilterChange('startDate', '')}>×</button>
            </span>
          )}
          {localFilters.endDate && (
            <span className="filter-tag">
              To: {localFilters.endDate}
              <button onClick={() => handleFilterChange('endDate', '')}>×</button>
            </span>
          )}
        </div>
      )}
    </div>
  );
};

