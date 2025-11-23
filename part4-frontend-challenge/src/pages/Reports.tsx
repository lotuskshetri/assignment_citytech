import React, { useState, useEffect, useRef } from 'react';
import { apiClient } from '../services/api';
import { revenueService } from '../services/revenueService';
// import { chartService } from '../services/chartService';
// import { LineChart } from '../components/charts/LineChart';
// import { PieChart } from '../components/charts/PieChart';
// import { RealTimeUpdates } from '../components/charts/RealTimeUpdates';
// import { DrillDown } from '../components/charts/DrillDown';
import './Reports.css';

export const Reports: React.FC = () => {
  const [data, setData] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const hasFetched = useRef(false);

  useEffect(() => {
    let mounted = true;

    const fetchData = async () => {
      // Prevent duplicate fetches in StrictMode
      if (hasFetched.current) {
        console.log('=== Skipping duplicate fetch (already fetched) ===');
        return;
      }

      hasFetched.current = true;

      try {
        console.log('=== Starting Analytics Fetch ===');
        setLoading(true);
        setError(null);

        const params = {
          startDate: '2025-11-16',
          endDate: '2025-11-23',
        };

        console.log('Fetching with params:', params);

        const volumeRes = await apiClient.get('/analytics/transactions/volume', { params });
        console.log('Volume response:', volumeRes.data);

        const successRes = await apiClient.get('/analytics/transactions/success-rate', { params });
        console.log('Success rate response:', successRes.data);
        console.log('Success rate keys:', Object.keys(successRes.data));
        console.log('Total transactions:', successRes.data.totalTransactions);
        console.log('Successful transactions:', successRes.data.successfulTransactions);
        console.log('Failed transactions:', successRes.data.failedTransactions);
        console.log('Success rate percentage:', successRes.data.successRate);

        const cardRes = await apiClient.get('/analytics/transactions/card-distribution', { params });
        console.log('Card distribution response:', cardRes.data);

        // Fetch revenue data
        console.log('Fetching revenue data...');
        const revenueByPeriod = await revenueService.getRevenueByPeriod({ ...params, period: 'daily' });
        console.log('Revenue by period:', revenueByPeriod);

        const revenueByMerchant = await revenueService.getRevenueByMerchant({ ...params, limit: 10 });
        console.log('Revenue by merchant:', revenueByMerchant);

        const revenueForecast = await revenueService.getRevenueForecast(7);
        console.log('Revenue forecast:', revenueForecast);

        const growthAnalysis = await revenueService.getGrowthAnalysis();
        console.log('Growth analysis:', growthAnalysis);

        const topPerformers = await revenueService.getTopPerformers({ ...params, limit: 5 });
        console.log('Top performers:', topPerformers);

        // Fetch chart data - TEMPORARILY DISABLED
        // console.log('Fetching chart data...');
        // const lineChartData = await chartService.getLineChartData({ ...params, metric: 'revenue', groupBy: 'day' });
        // console.log('Line chart data:', lineChartData);

        // const pieChartData = await chartService.getPieChartData({ ...params, distributeBy: 'status' });
        // console.log('Pie chart data:', pieChartData);

        if (!mounted) return;

        const fetchedData = {
          volume: volumeRes.data,
          successRate: successRes.data,
          cardDist: cardRes.data,
          revenueByPeriod,
          revenueByMerchant,
          revenueForecast,
          growthAnalysis,
          topPerformers,
          // lineChartData,
          // pieChartData,
        };

        console.log('Setting data:', fetchedData);
        setData(fetchedData);

      } catch (err: any) {
        console.error('=== Error Fetching Analytics ===');
        console.error('Error:', err);
        console.error('Error message:', err.message);
        console.error('Error response:', err.response);

        if (mounted) {
          setError(err.message || 'Failed to load data');
        }
      } finally {
        if (mounted) {
          console.log('=== Fetch Complete ===');
          setLoading(false);
        }
      }
    };

    fetchData();

    return () => {
      mounted = false;
    };
  }, []);

  const formatCurrency = (amount: number) => {
    if (!amount) return '$0.00';
    return `$${amount.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };

  console.log('=== Rendering Reports ===');
  console.log('Loading:', loading);
  console.log('Error:', error);
  console.log('Data:', data);

  try {
    return (
      <div className="analytics-page" style={{ padding: '24px', maxWidth: '1400px', margin: '0 auto' }}>
        <div style={{ marginBottom: '24px' }}>
          <h1 style={{ fontSize: '28px', margin: '0 0 8px 0' }}>📈 Analytics Dashboard</h1>
          <p style={{ color: '#6c757d', margin: 0 }}>Transaction analytics and performance metrics</p>
        </div>

        {error && (
          <div style={{
            padding: '16px',
            background: '#f8d7da',
            color: '#721c24',
            borderRadius: '8px',
            marginBottom: '20px'
          }}>
            Error: {error}
          </div>
        )}

        {loading && (
          <div style={{ textAlign: 'center', padding: '60px', color: '#6c757d' }}>
            Loading analytics data...
          </div>
        )}

        {!loading && !error && data && (
          <div>
            <div style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))',
              gap: '20px',
              marginBottom: '24px'
            }}>
              <div style={{ background: 'white', padding: '24px', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' }}>
                <div style={{ fontSize: '40px', marginBottom: '8px' }}>📊</div>
                <div style={{ fontSize: '14px', color: '#6c757d', marginBottom: '8px' }}>Total Transactions</div>
                <div style={{ fontSize: '28px', fontWeight: '700' }}>
                  {(data.cardDist?.totalTransactions || data.volume?.totalTransactions || 0).toLocaleString()}
                </div>
                <div style={{ fontSize: '13px', color: '#6c757d', marginTop: '4px' }}>
                  2025-11-16 to 2025-11-23
                </div>
              </div>

              <div style={{ background: 'white', padding: '24px', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' }}>
                <div style={{ fontSize: '40px', marginBottom: '8px' }}>💰</div>
                <div style={{ fontSize: '14px', color: '#6c757d', marginBottom: '8px' }}>Total Revenue</div>
                <div style={{ fontSize: '28px', fontWeight: '700' }}>
                  {formatCurrency(
                    data.volume?.totalAmount ||
                    data.cardDist?.distribution?.reduce((sum: number, card: any) => sum + (card.totalAmount || 0), 0) ||
                    0
                  )}
                </div>
                <div style={{ fontSize: '13px', color: '#6c757d', marginTop: '4px' }}>
                  Avg: {formatCurrency(data.volume?.averageAmount || 0)}
                </div>
              </div>

              <div style={{ background: 'white', padding: '24px', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' }}>
                <div style={{ fontSize: '40px', marginBottom: '8px' }}>✅</div>
                <div style={{ fontSize: '14px', color: '#6c757d', marginBottom: '8px' }}>Success Rate</div>
                <div style={{ fontSize: '28px', fontWeight: '700' }}>
                  {data.successRate?.successRate ? `${data.successRate.successRate.toFixed(1)}%` : '0%'}
                </div>
                <div style={{ fontSize: '13px', color: '#6c757d', marginTop: '4px' }}>
                  {(data.successRate?.completedCount || 0).toLocaleString()} completed
                </div>
              </div>

              <div style={{ background: 'white', padding: '24px', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' }}>
                <div style={{ fontSize: '40px', marginBottom: '8px' }}>⚠️</div>
                <div style={{ fontSize: '14px', color: '#6c757d', marginBottom: '8px' }}>Failed Transactions</div>
                <div style={{ fontSize: '28px', fontWeight: '700' }}>
                  {(data.successRate?.failedCount || 0).toLocaleString()}
                </div>
                <div style={{ fontSize: '13px', color: '#6c757d', marginTop: '4px' }}>
                  Requires attention
                </div>
              </div>
            </div>

            {data.cardDist?.distribution && Array.isArray(data.cardDist.distribution) && (
              <div style={{ background: 'white', padding: '24px', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)', marginBottom: '24px' }}>
                <h3 style={{ margin: '0 0 20px 0' }}>Card Type Distribution</h3>
                {data.cardDist.distribution.map((card: any) => (
                  <div key={card.cardType} style={{ marginBottom: '16px' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px' }}>
                      <span style={{ fontWeight: '600' }}>{card.cardType}</span>
                      <span>{card.percentage?.toFixed(1) || 0}%</span>
                    </div>
                    <div style={{ background: '#e9ecef', height: '24px', borderRadius: '4px', overflow: 'hidden' }}>
                      <div style={{
                        background: '#007bff',
                        height: '100%',
                        width: `${card.percentage || 0}%`
                      }}></div>
                    </div>
                    <div style={{ fontSize: '13px', color: '#6c757d', marginTop: '4px' }}>
                      {card.count || 0} transactions • {formatCurrency(card.totalAmount || 0)}
                    </div>
                  </div>
                ))}
              </div>
            )}

            {/* Revenue by Period */}
            {data.revenueByPeriod?.periods && Array.isArray(data.revenueByPeriod.periods) && (
              <div style={{ background: 'white', padding: '24px', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)', marginBottom: '24px' }}>
                <h3 style={{ margin: '0 0 8px 0' }}>💰 Revenue by Day</h3>
                <p style={{ color: '#6c757d', fontSize: '14px', marginBottom: '20px' }}>
                  Total: {formatCurrency(data.revenueByPeriod.totalRevenue || 0)} across {data.revenueByPeriod.totalTransactions || 0} transactions
                </p>
                <div style={{ display: 'grid', gap: '12px' }}>
                  {data.revenueByPeriod.periods.map((period: any) => (
                    <div key={period.period} style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center',
                      padding: '12px',
                      background: '#f8f9fa',
                      borderRadius: '4px'
                    }}>
                      <div>
                        <div style={{ fontWeight: '600', marginBottom: '4px' }}>{period.period}</div>
                        <div style={{ fontSize: '13px', color: '#6c757d' }}>
                          {period.transactionCount} transactions • Avg: {formatCurrency(period.averageTransaction)}
                        </div>
                      </div>
                      <div style={{ fontSize: '20px', fontWeight: '700', color: '#28a745' }}>
                        {formatCurrency(period.revenue)}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Top Performing Merchants */}
            {data.topPerformers?.topMerchants && Array.isArray(data.topPerformers.topMerchants) && (
              <div style={{ background: 'white', padding: '24px', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)', marginBottom: '24px' }}>
                <h3 style={{ margin: '0 0 8px 0' }}>🏆 Top Performing Merchants</h3>
                <p style={{ color: '#6c757d', fontSize: '14px', marginBottom: '20px' }}>
                  Ranked by {data.topPerformers.sortedBy} • {data.topPerformers.dateRange}
                </p>
                <div style={{ overflowX: 'auto' }}>
                  <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                    <thead>
                      <tr style={{ borderBottom: '2px solid #dee2e6' }}>
                        <th style={{ padding: '12px', textAlign: 'left', color: '#495057', fontWeight: '600' }}>Rank</th>
                        <th style={{ padding: '12px', textAlign: 'left', color: '#495057', fontWeight: '600' }}>Merchant ID</th>
                        <th style={{ padding: '12px', textAlign: 'right', color: '#495057', fontWeight: '600' }}>Revenue</th>
                        <th style={{ padding: '12px', textAlign: 'right', color: '#495057', fontWeight: '600' }}>Transactions</th>
                        <th style={{ padding: '12px', textAlign: 'right', color: '#495057', fontWeight: '600' }}>Avg Amount</th>
                        <th style={{ padding: '12px', textAlign: 'right', color: '#495057', fontWeight: '600' }}>% of Total</th>
                      </tr>
                    </thead>
                    <tbody>
                      {data.topPerformers.topMerchants.map((merchant: any) => (
                        <tr key={merchant.merchantId} style={{ borderBottom: '1px solid #e9ecef' }}>
                          <td style={{ padding: '12px' }}>
                            <span style={{
                              background: merchant.rank <= 3 ? '#ffd700' : '#e9ecef',
                              padding: '4px 8px',
                              borderRadius: '4px',
                              fontWeight: '600',
                              fontSize: '14px'
                            }}>
                              #{merchant.rank}
                            </span>
                          </td>
                          <td style={{ padding: '12px', fontFamily: 'monospace' }}>{merchant.merchantId}</td>
                          <td style={{ padding: '12px', textAlign: 'right', fontWeight: '600', color: '#28a745' }}>
                            {formatCurrency(merchant.revenue)}
                          </td>
                          <td style={{ padding: '12px', textAlign: 'right' }}>{merchant.transactionCount}</td>
                          <td style={{ padding: '12px', textAlign: 'right' }}>{formatCurrency(merchant.averageTransaction)}</td>
                          <td style={{ padding: '12px', textAlign: 'right' }}>{merchant.percentageOfTotal.toFixed(2)}%</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            )}

            {/* Revenue Forecast */}
            {data.revenueForecast?.forecast && Array.isArray(data.revenueForecast.forecast) && (
              <div style={{ background: 'white', padding: '24px', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)', marginBottom: '24px' }}>
                <h3 style={{ margin: '0 0 8px 0' }}>📈 Revenue Forecast (Next 7 Days)</h3>
                <p style={{ color: '#6c757d', fontSize: '14px', marginBottom: '20px' }}>
                  Method: {data.revenueForecast.method.replace('_', ' ')} • Confidence: {(data.revenueForecast.confidence * 100).toFixed(0)}%
                </p>
                <div style={{ display: 'grid', gap: '12px' }}>
                  {data.revenueForecast.forecast.map((forecast: any, index: number) => (
                    <div key={forecast.period} style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center',
                      padding: '12px',
                      background: index === 0 ? '#e7f3ff' : '#f8f9fa',
                      borderRadius: '4px',
                      borderLeft: index === 0 ? '4px solid #007bff' : 'none'
                    }}>
                      <div>
                        <div style={{ fontWeight: '600', marginBottom: '4px' }}>{forecast.period}</div>
                        <div style={{ fontSize: '12px', color: '#6c757d' }}>
                          Range: {formatCurrency(forecast.lowerBound)} - {formatCurrency(forecast.upperBound)}
                        </div>
                      </div>
                      <div style={{ fontSize: '18px', fontWeight: '700', color: '#007bff' }}>
                        {formatCurrency(forecast.predictedRevenue)}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Year-over-Year Growth */}
            {data.growthAnalysis && (
              <div style={{ background: 'white', padding: '24px', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)', marginBottom: '24px' }}>
                <h3 style={{ margin: '0 0 8px 0' }}>📊 Year-over-Year Growth Analysis</h3>
                <p style={{ color: '#6c757d', fontSize: '14px', marginBottom: '20px' }}>
                  Comparing {data.growthAnalysis.currentYear} vs {data.growthAnalysis.comparisonYear}
                </p>

                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '16px', marginBottom: '20px' }}>
                  <div style={{ padding: '16px', background: '#f8f9fa', borderRadius: '8px' }}>
                    <div style={{ fontSize: '14px', color: '#6c757d', marginBottom: '4px' }}>Current Year Total</div>
                    <div style={{ fontSize: '24px', fontWeight: '700', color: '#28a745' }}>
                      {formatCurrency(data.growthAnalysis.currentYearTotal)}
                    </div>
                  </div>
                  <div style={{ padding: '16px', background: '#f8f9fa', borderRadius: '8px' }}>
                    <div style={{ fontSize: '14px', color: '#6c757d', marginBottom: '4px' }}>Previous Year Total</div>
                    <div style={{ fontSize: '24px', fontWeight: '700' }}>
                      {formatCurrency(data.growthAnalysis.comparisonYearTotal)}
                    </div>
                  </div>
                  <div style={{ padding: '16px', background: '#f8f9fa', borderRadius: '8px' }}>
                    <div style={{ fontSize: '14px', color: '#6c757d', marginBottom: '4px' }}>Overall Growth Rate</div>
                    <div style={{ fontSize: '24px', fontWeight: '700', color: data.growthAnalysis.overallGrowthRate >= 0 ? '#28a745' : '#dc3545' }}>
                      {data.growthAnalysis.overallGrowthRate >= 0 ? '+' : ''}{data.growthAnalysis.overallGrowthRate.toFixed(1)}%
                    </div>
                  </div>
                </div>

                {data.growthAnalysis.currentYearTotal > 0 && (
                  <div>
                    <h4 style={{ fontSize: '16px', marginBottom: '12px' }}>Monthly Breakdown</h4>
                    <div style={{ overflowX: 'auto' }}>
                      {data.growthAnalysis.monthlyComparison
                        .filter((month: any) => month.currentYearRevenue > 0)
                        .map((month: any) => (
                          <div key={month.month} style={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            padding: '8px 0',
                            borderBottom: '1px solid #e9ecef'
                          }}>
                            <span style={{ fontWeight: '600' }}>{month.monthName}</span>
                            <span>{formatCurrency(month.currentYearRevenue)}</span>
                          </div>
                        ))}
                    </div>
                  </div>
                )}
              </div>
            )}

            {/* Interactive Charts Section - TEMPORARILY DISABLED */}
            {/*
            <div style={{ marginTop: '40px', paddingTop: '40px', borderTop: '3px solid #007bff' }}>
              <h2 style={{ fontSize: '24px', fontWeight: '700', color: '#212529', marginBottom: '8px' }}>
                📊 Interactive Charts & Analysis
              </h2>
              <p style={{ color: '#6c757d', fontSize: '14px', marginBottom: '32px' }}>
                Advanced visualizations and real-time monitoring
              </p>
            </div>
            */}
          </div>
        )}

        {!loading && !error && !data && (
          <div style={{ textAlign: 'center', padding: '60px', color: '#6c757d' }}>
            No data available
          </div>
        )}
      </div>
    );
  } catch (renderError: any) {
    console.error('=== RENDER ERROR ===', renderError);
    return (
      <div style={{ padding: '24px', color: 'red' }}>
        <h1>Render Error</h1>
        <pre>{JSON.stringify(renderError.message, null, 2)}</pre>
        <pre>{renderError.stack}</pre>
      </div>
    );
  }
};
