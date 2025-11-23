// Peak Times Heatmap Component

import React from 'react';
import { PeakTimesHeatmap } from '../../types/analytics.types';
import './PeakTimesChart.css';

interface PeakTimesChartProps {
  data: PeakTimesHeatmap | null;
  loading?: boolean;
}

export const PeakTimesChart: React.FC<PeakTimesChartProps> = ({ data, loading }) => {
  if (loading) {
    return (
      <div className="chart-container">
        <div className="chart-header">
          <h3>Peak Transaction Times</h3>
        </div>
        <div className="chart-loading">Loading...</div>
      </div>
    );
  }

  if (!data) {
    return (
      <div className="chart-container">
        <div className="chart-header">
          <h3>Peak Transaction Times</h3>
        </div>
        <div className="chart-empty">No data available</div>
      </div>
    );
  }

  const formatHour = (hour: number) => {
    const period = hour >= 12 ? 'PM' : 'AM';
    const displayHour = hour === 0 ? 12 : hour > 12 ? hour - 12 : hour;
    return `${displayHour}${period}`;
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount);
  };

  const maxHourlyCount = Math.max(...data.hourlyData.map(h => h.transactionCount), 1);
  const maxDailyCount = Math.max(...data.dailyData.map(d => d.transactionCount), 1);

  return (
    <div className="chart-container">
      <div className="chart-header">
        <h3>Peak Transaction Times</h3>
        <span className="chart-subtitle">
          Peak: {data.peakDay} at {formatHour(data.peakHour)}
        </span>
      </div>

      <div className="peak-times-chart">
        <div className="chart-section">
          <h4>By Hour of Day</h4>
          <div className="hourly-bars">
            {data.hourlyData.map((hour) => (
              <div key={hour.hour} className="hour-bar">
                <div className="bar-container">
                  <div
                    className="bar-fill"
                    style={{
                      height: `${(hour.transactionCount / maxHourlyCount) * 100}%`,
                      backgroundColor: hour.hour === data.peakHour ? '#007bff' : '#6c757d'
                    }}
                    title={`${hour.transactionCount} transactions, avg ${formatCurrency(hour.averageAmount)}`}
                  ></div>
                </div>
                <div className="bar-label">{formatHour(hour.hour)}</div>
              </div>
            ))}
          </div>
        </div>

        <div className="chart-section">
          <h4>By Day of Week</h4>
          <div className="daily-bars">
            {data.dailyData.map((day) => (
              <div key={day.dayOfWeek} className="day-bar">
                <div className="day-info">
                  <span className="day-name">{day.dayOfWeek}</span>
                  <span className="day-count">{day.transactionCount}</span>
                </div>
                <div className="day-bar-container">
                  <div
                    className="day-bar-fill"
                    style={{
                      width: `${(day.transactionCount / maxDailyCount) * 100}%`,
                      backgroundColor: day.dayOfWeek === data.peakDay ? '#007bff' : '#6c757d'
                    }}
                  ></div>
                </div>
                <span className="day-amount">{formatCurrency(day.averageAmount)}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

