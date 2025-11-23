// Drill-Down Component for detailed analysis

import React, { useState } from 'react';
import { chartService, ChartDataResponse } from '../../services/chartService';
import { LineChart } from './LineChart';
import './DrillDown.css';

interface DrillDownProps {
  dateRange: {
    startDate: string;
    endDate: string;
  };
}

export const DrillDown: React.FC<DrillDownProps> = ({ dateRange }) => {
  const [category, setCategory] = useState<string>('merchant');
  const [categoryValue, setCategoryValue] = useState<string>('');
  const [drillDownData, setDrillDownData] = useState<ChartDataResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const merchantSuggestions = ['MCH-00001', 'MCH-00002', 'MCH-00009', 'MCH-00012', 'MCH-00013'];
  const cardTypeSuggestions = ['VISA', 'MASTERCARD', 'AMEX', 'DISCOVER'];
  const statusSuggestions = ['completed', 'pending', 'failed'];

  const getSuggestions = () => {
    switch (category) {
      case 'merchant':
        return merchantSuggestions;
      case 'cardtype':
        return cardTypeSuggestions;
      case 'status':
        return statusSuggestions;
      default:
        return [];
    }
  };

  const handleDrillDown = async () => {
    if (!categoryValue.trim()) {
      setError('Please enter a value to drill down');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const data = await chartService.getDrillDownData(
        category,
        categoryValue,
        dateRange
      );
      setDrillDownData(data);
    } catch (err: any) {
      console.error('Drill-down error:', err);
      setError(err.response?.data?.message || 'Failed to fetch drill-down data');
      setDrillDownData(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="drilldown-container">
      <h4>🔍 Drill-Down Analysis</h4>
      <p className="drilldown-description">
        Get detailed breakdown for a specific merchant, card type, or status
      </p>

      <div className="drilldown-controls">
        <div className="control-group">
          <label>Category</label>
          <select
            value={category}
            onChange={(e) => {
              setCategory(e.target.value);
              setCategoryValue('');
              setDrillDownData(null);
            }}
            className="drilldown-select"
          >
            <option value="merchant">Merchant</option>
            <option value="cardtype">Card Type</option>
            <option value="status">Status</option>
          </select>
        </div>

        <div className="control-group">
          <label>Value</label>
          <div className="value-input-group">
            <input
              type="text"
              value={categoryValue}
              onChange={(e) => setCategoryValue(e.target.value)}
              placeholder={`Enter ${category}...`}
              className="drilldown-input"
              list={`${category}-suggestions`}
            />
            <datalist id={`${category}-suggestions`}>
              {getSuggestions().map((suggestion) => (
                <option key={suggestion} value={suggestion} />
              ))}
            </datalist>
          </div>
        </div>

        <button
          onClick={handleDrillDown}
          disabled={loading || !categoryValue.trim()}
          className="drilldown-button"
        >
          {loading ? 'Loading...' : 'Drill Down'}
        </button>
      </div>

      {/* Quick suggestions */}
      <div className="quick-suggestions">
        <span className="suggestions-label">Quick select:</span>
        {getSuggestions().slice(0, 5).map((suggestion) => (
          <button
            key={suggestion}
            onClick={() => {
              setCategoryValue(suggestion);
              setDrillDownData(null);
            }}
            className="suggestion-chip"
          >
            {suggestion}
          </button>
        ))}
      </div>

      {error && (
        <div className="drilldown-error">
          ⚠️ {error}
        </div>
      )}

      {loading && (
        <div className="drilldown-loading">
          <div className="loading-spinner"></div>
          <p>Analyzing {categoryValue}...</p>
        </div>
      )}

      {drillDownData && !loading && (
        <div className="drilldown-results">
          <div className="results-header">
            <h5>
              Results for: <span className="highlight">{categoryValue}</span>
            </h5>
            <span className="results-date">{dateRange.startDate} to {dateRange.endDate}</span>
          </div>

          <LineChart
            data={drillDownData}
            title={`Detailed Analysis: ${categoryValue}`}
          />

          {drillDownData.datasets.length > 0 && (
            <div className="results-summary">
              <div className="summary-card">
                <div className="summary-label">Total Data Points</div>
                <div className="summary-value">{drillDownData.labels.length}</div>
              </div>
              {drillDownData.datasets.map((dataset: any, index: number) => {
                const total = dataset.data.reduce((sum: number, val: number) => sum + val, 0);
                const avg = total / dataset.data.length;
                return (
                  <div key={index} className="summary-card">
                    <div className="summary-label">{dataset.label}</div>
                    <div className="summary-value">
                      Avg: {avg.toFixed(2)}
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

