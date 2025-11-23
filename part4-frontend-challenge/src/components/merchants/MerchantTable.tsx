// filepath: src/components/merchants/MerchantTable.tsx

import React from 'react';
import { MerchantStats } from '../../types/merchant.types';
import './MerchantTable.css';

interface MerchantTableProps {
  merchants: MerchantStats[];
  loading: boolean;
  onMerchantClick?: (merchantId: string) => void;
}

export const MerchantTable: React.FC<MerchantTableProps> = ({
  merchants,
  loading,
  onMerchantClick,
}) => {
  if (loading) {
    return (
      <div className="merchant-table-loading">
        <div className="spinner"></div>
        <p>Loading merchants...</p>
      </div>
    );
  }

  if (merchants.length === 0) {
    return (
      <div className="merchant-table-empty">
        <p>No merchants found</p>
      </div>
    );
  }

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(amount);
  };

  const formatDate = (dateStr: string) => {
    return new Date(dateStr).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  return (
    <div className="merchant-table-container">
      <table className="merchant-table">
        <thead>
          <tr>
            <th>Merchant ID</th>
            <th>Merchant Name</th>
            <th>Total Revenue</th>
            <th>Transactions</th>
            <th>Success Rate</th>
            <th>Avg Amount</th>
            <th>Last Transaction</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {merchants.map((merchant) => (
            <tr
              key={merchant.merchantId}
              onClick={() => onMerchantClick?.(merchant.merchantId)}
              className="merchant-row"
            >
              <td className="merchant-id">{merchant.merchantId}</td>
              <td className="merchant-name">{merchant.merchantName}</td>
              <td className="merchant-revenue">
                {formatCurrency(merchant.totalRevenue)}
              </td>
              <td className="merchant-transactions">
                <div className="transaction-breakdown">
                  <span className="total">{merchant.totalTransactions}</span>
                  <span className="completed">✓ {merchant.completedCount}</span>
                  {merchant.failedCount > 0 && (
                    <span className="failed">✗ {merchant.failedCount}</span>
                  )}
                </div>
              </td>
              <td className="merchant-success-rate">
                <div className="progress-bar">
                  <div
                    className="progress-fill"
                    style={{ width: `${merchant.successRate}%` }}
                  ></div>
                </div>
                <span>{merchant.successRate.toFixed(1)}%</span>
              </td>
              <td className="merchant-avg-amount">
                {formatCurrency(merchant.averageTransactionAmount)}
              </td>
              <td className="merchant-last-txn">
                {formatDate(merchant.lastTransactionDate)}
              </td>
              <td className="merchant-status">
                <span className={`status-badge status-${merchant.status}`}>
                  {merchant.status}
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

