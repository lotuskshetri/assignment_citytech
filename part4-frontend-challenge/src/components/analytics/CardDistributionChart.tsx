// Card Distribution Chart Component

import React from 'react';
import { CardDistribution } from '../../types/analytics.types';
import './CardDistributionChart.css';

interface CardDistributionChartProps {
  data: CardDistribution | null;
  loading?: boolean;
}

export const CardDistributionChart: React.FC<CardDistributionChartProps> = ({ data, loading }) => {
  if (loading) {
    return (
      <div className="chart-container">
        <div className="chart-header">
          <h3>Card Type Distribution</h3>
        </div>
        <div className="chart-loading">Loading...</div>
      </div>
    );
  }

  if (!data || !data.distribution || data.distribution.length === 0) {
    return (
      <div className="chart-container">
        <div className="chart-header">
          <h3>Card Type Distribution</h3>
        </div>
        <div className="chart-empty">No data available</div>
      </div>
    );
  }

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount);
  };

  const getCardColor = (cardType: string) => {
    const colors: Record<string, string> = {
      VISA: '#1A1F71',
      MASTERCARD: '#EB001B',
      AMEX: '#006FCF',
      DISCOVER: '#FF6000',
    };
    return colors[cardType] || '#6c757d';
  };

  return (
    <div className="chart-container">
      <div className="chart-header">
        <h3>Card Type Distribution</h3>
        <span className="chart-subtitle">Total: {data.totalTransactions} transactions</span>
      </div>

      <div className="card-distribution-chart">
        {data.distribution.map((card) => (
          <div key={card.cardType} className="card-item">
            <div className="card-info">
              <div
                className="card-color-dot"
                style={{ backgroundColor: getCardColor(card.cardType) }}
              ></div>
              <div className="card-details">
                <div className="card-type">{card.cardType}</div>
                <div className="card-stats">
                  {card.count} transactions • {formatCurrency(card.totalAmount)}
                </div>
              </div>
            </div>
            <div className="card-percentage">
              <div className="percentage-bar">
                <div
                  className="percentage-fill"
                  style={{
                    width: `${card.percentage}%`,
                    backgroundColor: getCardColor(card.cardType)
                  }}
                ></div>
              </div>
              <span className="percentage-value">{card.percentage.toFixed(1)}%</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

