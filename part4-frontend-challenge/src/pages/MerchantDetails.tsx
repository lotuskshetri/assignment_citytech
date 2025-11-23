// Merchant Details Page Component

import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { merchantService } from '../services/merchantService';
import { MerchantEditForm } from '../components/merchants/MerchantEditForm';
import './MerchantDetails.css';

interface MerchantDetails {
  merchantId: string;
  merchantName: string;
  email?: string;
  phone?: string;
  address?: string;
  businessName?: string;
  registrationNumber?: string;
  status: string;
  totalTransactions: number;
  totalRevenue: number;
  completedCount: number;
  failedCount: number;
  pendingCount: number;
  successRate: number;
  averageTransactionAmount: number;
  lastTransactionDate?: string;
  firstTransactionDate?: string;
}

interface Transaction {
  txnId: number;
  amount: number;
  currency: string;
  status: string;
  cardType: string;
  txnDate: string;
  authCode: string;
}

export const MerchantDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [merchant, setMerchant] = useState<MerchantDetails | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [activeTab, setActiveTab] = useState<'overview' | 'transactions' | 'activity'>('overview');

  useEffect(() => {
    if (id) {
      fetchMerchantDetails();
    }
  }, [id]);

  const fetchMerchantDetails = async () => {
    try {
      setLoading(true);
      setError(null);

      // Fetch full merchant details (now includes both profile and basic stats)
      const response = await fetch(`/api/v1/merchants/${id}`);
      if (!response.ok) {
        throw new Error('Merchant not found');
      }
      const merchantData = await response.json();

      // Format address from address fields
      const formattedAddress = merchantData.addressLine1
        ? `${merchantData.addressLine1}${merchantData.addressLine2 ? ', ' + merchantData.addressLine2 : ''}, ${merchantData.city || ''}, ${merchantData.state || ''} ${merchantData.postalCode || ''}`.trim()
        : undefined;

      // Map backend fields to frontend interface
      const details: MerchantDetails = {
        merchantId: merchantData.merchantId,
        merchantName: merchantData.merchantName,
        businessName: merchantData.businessName || merchantData.merchantName,
        email: merchantData.email,
        phone: merchantData.phone,
        address: formattedAddress,
        registrationNumber: merchantData.taxId,
        status: merchantData.status || 'active',
        // Note: Statistics will be 0 if not in response, that's OK
        totalTransactions: merchantData.totalTransactions || 0,
        totalRevenue: merchantData.totalRevenue || 0,
        completedCount: merchantData.completedCount || 0,
        failedCount: merchantData.failedCount || 0,
        pendingCount: merchantData.pendingCount || 0,
        successRate: merchantData.successRate || 0,
        averageTransactionAmount: merchantData.averageTransactionAmount || 0,
        lastTransactionDate: merchantData.lastTransactionDate,
        firstTransactionDate: merchantData.firstTransactionDate,
      };

      setMerchant(details);

      // Fetch recent transactions
      // Using the transactions endpoint with merchant filter
      const txnResponse = await fetch(`/api/v1/transactions?merchantId=${id}&page=0&size=10`);
      if (txnResponse.ok) {
        const txnData = await txnResponse.json();
        setTransactions(txnData.transactions || []);
      }
    } catch (err: any) {
      console.error('Error fetching merchant details:', err);
      setError(err.message || 'Failed to load merchant details');
    } finally {
      setLoading(false);
    }
  };

  const handleEditComplete = () => {
    setIsEditing(false);
    fetchMerchantDetails(); // Refresh data
  };

  const handleExportTransactions = () => {
    // TODO: Implement CSV export
    alert('Export feature coming soon!');
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(amount);
  };

  const formatDate = (dateString?: string) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString();
  };

  if (loading) {
    return (
      <div className="merchant-details-page">
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p>Loading merchant details...</p>
        </div>
      </div>
    );
  }

  if (error || !merchant) {
    return (
      <div className="merchant-details-page">
        <div className="error-container">
          <span className="error-icon">⚠️</span>
          <h2>Error Loading Merchant</h2>
          <p>{error || 'Merchant not found'}</p>
          <button onClick={() => navigate('/merchants')} className="btn-back">
            ← Back to Merchants
          </button>
        </div>
      </div>
    );
  }

  if (isEditing) {
    return (
      <div className="merchant-details-page">
        <MerchantEditForm
          merchant={merchant}
          onCancel={() => setIsEditing(false)}
          onSave={handleEditComplete}
        />
      </div>
    );
  }

  return (
    <div className="merchant-details-page">
      {/* Header */}
      <div className="details-header">
        <button onClick={() => navigate('/merchants')} className="btn-back">
          ← Back to Merchants
        </button>
        <div className="header-actions">
          <button onClick={() => setIsEditing(true)} className="btn-edit">
            ✏️ Edit Merchant
          </button>
        </div>
      </div>

      {/* Merchant Profile Card */}
      <div className="merchant-profile-card">
        <div className="profile-header">
          <div className="merchant-avatar">
            {merchant.merchantName?.charAt(0) || 'M'}
          </div>
          <div className="profile-info">
            <h1>{merchant.merchantName || merchant.merchantId}</h1>
            <p className="merchant-id">{merchant.merchantId}</p>
            <span className={`status-badge status-${merchant.status.toLowerCase()}`}>
              {merchant.status}
            </span>
          </div>
        </div>

        <div className="profile-details">
          <div className="detail-row">
            <span className="detail-label">Business Name:</span>
            <span className="detail-value">{merchant.businessName || 'N/A'}</span>
          </div>
          <div className="detail-row">
            <span className="detail-label">Email:</span>
            <span className="detail-value">{merchant.email || 'N/A'}</span>
          </div>
          <div className="detail-row">
            <span className="detail-label">Phone:</span>
            <span className="detail-value">{merchant.phone || 'N/A'}</span>
          </div>
          <div className="detail-row">
            <span className="detail-label">Address:</span>
            <span className="detail-value">{merchant.address || 'N/A'}</span>
          </div>
          <div className="detail-row">
            <span className="detail-label">Registration #:</span>
            <span className="detail-value">{merchant.registrationNumber || 'N/A'}</span>
          </div>
        </div>
      </div>

      {/* Statistics Cards */}
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon">📊</div>
          <div className="stat-content">
            <div className="stat-label">Total Transactions</div>
            <div className="stat-value">{merchant.totalTransactions.toLocaleString()}</div>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">💰</div>
          <div className="stat-content">
            <div className="stat-label">Total Revenue</div>
            <div className="stat-value">{formatCurrency(merchant.totalRevenue)}</div>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">✅</div>
          <div className="stat-content">
            <div className="stat-label">Success Rate</div>
            <div className="stat-value">{merchant.successRate.toFixed(1)}%</div>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">📈</div>
          <div className="stat-content">
            <div className="stat-label">Avg Transaction</div>
            <div className="stat-value">{formatCurrency(merchant.averageTransactionAmount)}</div>
          </div>
        </div>
      </div>

      {/* Status Breakdown */}
      <div className="status-breakdown">
        <div className="breakdown-item completed">
          <span className="breakdown-label">Completed</span>
          <span className="breakdown-value">{merchant.completedCount}</span>
        </div>
        <div className="breakdown-item pending">
          <span className="breakdown-label">Pending</span>
          <span className="breakdown-value">{merchant.pendingCount}</span>
        </div>
        <div className="breakdown-item failed">
          <span className="breakdown-label">Failed</span>
          <span className="breakdown-value">{merchant.failedCount}</span>
        </div>
      </div>

      {/* Tabs */}
      <div className="details-tabs">
        <button
          className={`tab ${activeTab === 'overview' ? 'active' : ''}`}
          onClick={() => setActiveTab('overview')}
        >
          Overview
        </button>
        <button
          className={`tab ${activeTab === 'transactions' ? 'active' : ''}`}
          onClick={() => setActiveTab('transactions')}
        >
          Recent Transactions
        </button>
        <button
          className={`tab ${activeTab === 'activity' ? 'active' : ''}`}
          onClick={() => setActiveTab('activity')}
        >
          Activity Timeline
        </button>
      </div>

      {/* Tab Content */}
      <div className="tab-content">
        {activeTab === 'overview' && (
          <div className="overview-content">
            <div className="overview-section">
              <h3>Account Activity</h3>
              <div className="activity-item">
                <span>First Transaction:</span>
                <span>{formatDate(merchant.firstTransactionDate)}</span>
              </div>
              <div className="activity-item">
                <span>Last Transaction:</span>
                <span>{formatDate(merchant.lastTransactionDate)}</span>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'transactions' && (
          <div className="transactions-content">
            <div className="transactions-header">
              <h3>Recent Transactions ({transactions.length})</h3>
              <button onClick={handleExportTransactions} className="btn-export">
                📥 Export History
              </button>
            </div>

            {transactions.length > 0 ? (
              <div className="transactions-table">
                <table>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Date</th>
                      <th>Amount</th>
                      <th>Card Type</th>
                      <th>Status</th>
                      <th>Auth Code</th>
                    </tr>
                  </thead>
                  <tbody>
                    {transactions.map((txn) => (
                      <tr key={txn.txnId}>
                        <td>{txn.txnId}</td>
                        <td>{formatDate(txn.txnDate)}</td>
                        <td>{formatCurrency(txn.amount)}</td>
                        <td>{txn.cardType}</td>
                        <td>
                          <span className={`status-badge status-${txn.status.toLowerCase()}`}>
                            {txn.status}
                          </span>
                        </td>
                        <td>{txn.authCode}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            ) : (
              <div className="no-transactions">
                <p>No recent transactions found</p>
              </div>
            )}
          </div>
        )}

        {activeTab === 'activity' && (
          <div className="activity-content">
            <div className="timeline">
              <div className="timeline-item">
                <div className="timeline-dot"></div>
                <div className="timeline-content">
                  <div className="timeline-date">{formatDate(merchant.lastTransactionDate)}</div>
                  <div className="timeline-text">Latest transaction processed</div>
                </div>
              </div>
              <div className="timeline-item">
                <div className="timeline-dot"></div>
                <div className="timeline-content">
                  <div className="timeline-date">{formatDate(merchant.firstTransactionDate)}</div>
                  <div className="timeline-text">First transaction processed</div>
                </div>
              </div>
              <div className="timeline-item">
                <div className="timeline-dot"></div>
                <div className="timeline-content">
                  <div className="timeline-date">Status: {merchant.status}</div>
                  <div className="timeline-text">Merchant account {merchant.status.toLowerCase()}</div>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

