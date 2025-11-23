import './TransactionSummary.css';

interface Transaction {
  amount: number;
  currency: string;
  status: string;
}

interface SummaryData {
  totalCount: number;
  totalAmount: number;
  completedCount: number;
  pendingCount: number;
  failedCount: number;
}

interface TransactionSummaryProps {
  transactions: Transaction[];
  totalTransactions: number;
  summary?: SummaryData; // Summary from API
}

export const TransactionSummary = ({ transactions, totalTransactions, summary }: TransactionSummaryProps) => {
  // Use summary from API if available, otherwise calculate from current page (fallback)
  const totalAmount = summary?.totalAmount ?? transactions.reduce((sum, txn) => sum + txn.amount, 0);
  const currency = transactions[0]?.currency || 'USD';
  
  const completedCount = summary?.completedCount ?? transactions.filter(txn => txn.status.toLowerCase() === 'completed').length;
  const pendingCount = summary?.pendingCount ?? transactions.filter(txn => txn.status.toLowerCase() === 'pending').length;
  const failedCount = summary?.failedCount ?? transactions.filter(txn => txn.status.toLowerCase() === 'failed').length;

  const displayTotalCount = summary?.totalCount ?? totalTransactions;

  const formatAmount = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: currency,
    }).format(amount);
  };

  return (
    <div className="transaction-summary">
      <div className="summary-card">
        <div className="summary-label">Total Transactions</div>
        <div className="summary-value">{displayTotalCount.toLocaleString()}</div>
        <div className="summary-hint">Across all filters</div>
      </div>

      <div className="summary-card">
        <div className="summary-label">Total Amount</div>
        <div className="summary-value amount">{formatAmount(totalAmount)}</div>
        <div className="summary-hint">Sum of all filtered transactions</div>
      </div>

      <div className="summary-card">
        <div className="summary-label">Completed</div>
        <div className="summary-value completed">{completedCount.toLocaleString()}</div>
        <div className="summary-hint">{displayTotalCount > 0 ? ((completedCount / displayTotalCount) * 100).toFixed(1) : 0}%</div>
      </div>

      <div className="summary-card">
        <div className="summary-label">Pending</div>
        <div className="summary-value pending">{pendingCount.toLocaleString()}</div>
        <div className="summary-hint">{displayTotalCount > 0 ? ((pendingCount / displayTotalCount) * 100).toFixed(1) : 0}%</div>
      </div>

      <div className="summary-card">
        <div className="summary-label">Failed</div>
        <div className="summary-value failed">{failedCount.toLocaleString()}</div>
        <div className="summary-hint">{displayTotalCount > 0 ? ((failedCount / displayTotalCount) * 100).toFixed(1) : 0}%</div>
      </div>
    </div>
  );
};
