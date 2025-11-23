// Simple Pie Chart Component (SVG-based)

import React from 'react';
import './PieChart.css';

interface PieChartProps {
  data: {
    labels: string[];
    datasets: Array<{
      label: string;
      data: number[];
      backgroundColor?: string | string[];
    }>;
  };
  title: string;
}

export const PieChart: React.FC<PieChartProps> = ({ data, title }) => {
  if (!data || !data.labels || !data.datasets || data.datasets.length === 0) {
    return <div className="chart-error">No data available</div>;
  }

  const dataset = data.datasets[0];
  const values = dataset.data;
  const total = values.reduce((sum, val) => sum + val, 0);

  const centerX = 200;
  const centerY = 150;
  const radius = 100;

  const defaultColors = [
    '#007bff', '#28a745', '#dc3545', '#ffc107', '#17a2b8',
    '#6610f2', '#e83e8c', '#fd7e14', '#20c997', '#6c757d'
  ];

  const colors = Array.isArray(dataset.backgroundColor)
    ? dataset.backgroundColor
    : values.map((_, i) => defaultColors[i % defaultColors.length]);

  // Calculate slices
  let currentAngle = -90; // Start at top
  const slices = values.map((value, index) => {
    const percentage = (value / total) * 100;
    const angle = (value / total) * 360;
    const startAngle = currentAngle;
    const endAngle = currentAngle + angle;

    currentAngle = endAngle;

    // Calculate path for slice
    const startRad = (startAngle * Math.PI) / 180;
    const endRad = (endAngle * Math.PI) / 180;

    const x1 = centerX + radius * Math.cos(startRad);
    const y1 = centerY + radius * Math.sin(startRad);
    const x2 = centerX + radius * Math.cos(endRad);
    const y2 = centerY + radius * Math.sin(endRad);

    const largeArc = angle > 180 ? 1 : 0;

    const pathData = [
      `M ${centerX} ${centerY}`,
      `L ${x1} ${y1}`,
      `A ${radius} ${radius} 0 ${largeArc} 1 ${x2} ${y2}`,
      'Z'
    ].join(' ');

    // Calculate label position
    const labelAngle = (startAngle + endAngle) / 2;
    const labelRad = (labelAngle * Math.PI) / 180;
    const labelRadius = radius + 30;
    const labelX = centerX + labelRadius * Math.cos(labelRad);
    const labelY = centerY + labelRadius * Math.sin(labelRad);

    return {
      pathData,
      color: colors[index],
      label: data.labels[index],
      value,
      percentage,
      labelX,
      labelY,
    };
  });

  const formatValue = (value: number) => {
    if (value >= 1000000) {
      return `${(value / 1000000).toFixed(1)}M`;
    } else if (value >= 1000) {
      return `${(value / 1000).toFixed(1)}K`;
    }
    return value.toLocaleString();
  };

  return (
    <div className="pie-chart-container">
      <h4>{title}</h4>
      <div className="pie-chart-content">
        <svg width="400" height="300" className="pie-chart">
          {slices.map((slice, index) => (
            <g key={index}>
              <path
                d={slice.pathData}
                fill={slice.color}
                stroke="white"
                strokeWidth="2"
                className="pie-slice"
              >
                <title>
                  {slice.label}: {formatValue(slice.value)} ({slice.percentage.toFixed(1)}%)
                </title>
              </path>
            </g>
          ))}
        </svg>

        <div className="pie-legend">
          {slices.map((slice, index) => (
            <div key={index} className="legend-item">
              <div
                className="legend-color"
                style={{ backgroundColor: slice.color }}
              ></div>
              <div className="legend-text">
                <div className="legend-label">{slice.label}</div>
                <div className="legend-value">
                  {formatValue(slice.value)} ({slice.percentage.toFixed(1)}%)
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

