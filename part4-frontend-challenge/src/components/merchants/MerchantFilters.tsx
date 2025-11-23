// filepath: src/components/merchants/MerchantFilters.tsx

import React, { useState } from 'react';
import './MerchantFilters.css';

interface MerchantFiltersProps {
  onFilterChange: (search: string) => void;
  onRefresh: () => void;
}

export const MerchantFilters: React.FC<MerchantFiltersProps> = ({
  onFilterChange,
  onRefresh,
}) => {
  const [searchTerm, setSearchTerm] = useState('');

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setSearchTerm(value);
    onFilterChange(value);
  };

  const handleClearSearch = () => {
    setSearchTerm('');
    onFilterChange('');
  };

  return (
    <div className="merchant-filters">
      <div className="search-box">
        <span className="search-icon">🔍</span>
        <input
          type="text"
          placeholder="Search by Merchant ID or Name..."
          value={searchTerm}
          onChange={handleSearchChange}
          className="search-input"
        />
        {searchTerm && (
          <button onClick={handleClearSearch} className="clear-btn" title="Clear search">
            ✕
          </button>
        )}
      </div>

      <button onClick={onRefresh} className="refresh-btn" title="Refresh data">
        ↻ Refresh
      </button>
    </div>
  );
};

