// Analytics Stat Cards Component

import React from 'react';
import './StatCards.css';

interface StatCardData {
  title: string;
  value: string | number;
  subtitle?: string;
  icon: string;
  trend?: {
    value: number;
    isPositive: boolean;
  };
}

interface StatCardsProps {
  cards: StatCardData[];
  loading?: boolean;
}

export const StatCards: React.FC<StatCardsProps> = ({ cards, loading }) => {
  if (loading) {
    return (
      <div className="stat-cards">
        {[1, 2, 3, 4].map((i) => (
          <div key={i} className="stat-card loading">
            <div className="skeleton-box"></div>
          </div>
        ))}
      </div>
    );
  }

  return (
    <div className="stat-cards">
      {cards.map((card, index) => (
        <div key={index} className="stat-card">
          <div className="stat-icon">{card.icon}</div>
          <div className="stat-content">
            <div className="stat-title">{card.title}</div>
            <div className="stat-value">{card.value}</div>
            {card.subtitle && <div className="stat-subtitle">{card.subtitle}</div>}
            {card.trend && (
              <div className={`stat-trend ${card.trend.isPositive ? 'positive' : 'negative'}`}>
                {card.trend.isPositive ? '↑' : '↓'} {Math.abs(card.trend.value)}%
              </div>
            )}
          </div>
        </div>
      ))}
    </div>
  );
};

