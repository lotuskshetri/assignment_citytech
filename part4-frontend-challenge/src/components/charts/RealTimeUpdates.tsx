// Real-Time Transaction Updates Component

import React, { useState, useEffect } from 'react';
import { chartService } from '../../services/chartService';
import './RealTimeUpdates.css';

export const RealTimeUpdates: React.FC = () => {
  const [transactions, setTransactions] = useState<any[]>([]);
  const [lastUpdate, setLastUpdate] = useState<Date>(new Date());
  const [isPolling, setIsPolling] = useState(true);

  useEffect(() => {
    let interval: number;

    const fetchRecentTransactions = async () => {
      try {
        // Get transactions from last 5 minutes
        const since = new Date(Date.now() - 5 * 60 * 1000).toISOString();
        const recentTxns = await chartService.getRecentTransactions(since, 10);

        if (recentTxns && recentTxns.length > 0) {
          setTransactions(recentTxns);
          setLastUpdate(new Date());
        }
      } catch (error) {
        console.error('Error fetching recent transactions:', error);
      }
    };

    if (isPolling) {
      fetchRecentTransactions();
      interval = setInterval(fetchRecentTransactions, 10000); // Poll every 10 seconds
    }

    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isPolling]);

  const formatCurrency = (amount: number) => {
    return `$${amount.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };

  const formatTime = (timestamp: string) => {
    return new Date(timestamp).toLocaleTimeString();
  };

  const getStatusColor = (status: string) => {
    const colors: Record<string, string> = {
      completed: '#28a745',
      pending: '#ffc107',
      failed: '#dc3545',
    };
    return colors[status.toLowerCase()] || '#6c757d';
  };

  return (
    <div className="realtime-updates">
      <div className="realtime-header">
        <h4>🔴 Real-Time Transaction Updates</h4>
        <div className="realtime-controls">
          <span className="last-update">
            Last update: {lastUpdate.toLocaleTimeString()}
          </span>
          <button
            onClick={() => setIsPolling(!isPolling)}
            className={`polling-toggle ${isPolling ? 'active' : ''}`}
          >
            {isPolling ? '⏸ Pause' : '▶ Resume'}
          </button>
        </div>
      </div>

      {transactions.length === 0 ? (
        <div className="no-transactions">
          <div className="pulse-indicator"></div>
          <p>Waiting for new transactions...</p>
          <p className="hint">Monitoring last 5 minutes</p>
        </div>
      ) : (
        <div className="transactions-stream">
          {transactions.map((txn, index) => (
            <div key={txn.txnId || index} className="transaction-item">
              <div className="txn-badge" style={{ backgroundColor: getStatusColor(txn.status) }}>
                {txn.status?.toUpperCase()}
              </div>
              <div className="txn-details">
                <div className="txn-main">
                  <span className="txn-merchant">{txn.merchantId}</span>
                  <span className="txn-amount">{formatCurrency(txn.amount)}</span>
                </div>
                <div className="txn-meta">
                  <span>{txn.cardType}</span>
                  <span>•</span>
                  <span>{formatTime(txn.localTxnDateTime || txn.txnDate)}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

