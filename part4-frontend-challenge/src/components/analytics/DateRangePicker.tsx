// Analytics Date Range Picker Component

import React, { useState } from 'react';
import './DateRangePicker.css';

interface DateRangePickerProps {
  startDate: string;
  endDate: string;
  onDateChange: (startDate: string, endDate: string) => void;
}

export const DateRangePicker: React.FC<DateRangePickerProps> = ({
  startDate,
  endDate,
  onDateChange,
}) => {
  const [localStart, setLocalStart] = useState(startDate);
  const [localEnd, setLocalEnd] = useState(endDate);

  const handleApply = () => {
    onDateChange(localStart, localEnd);
  };

  const setPresetRange = (days: number) => {
    const end = new Date();
    const start = new Date();
    start.setDate(end.getDate() - days);

    const endStr = end.toISOString().split('T')[0];
    const startStr = start.toISOString().split('T')[0];

    setLocalStart(startStr);
    setLocalEnd(endStr);
    onDateChange(startStr, endStr);
  };

  return (
    <div className="date-range-picker">
      <div className="date-inputs">
        <div className="date-input-group">
          <label>Start Date</label>
          <input
            type="date"
            value={localStart}
            onChange={(e) => setLocalStart(e.target.value)}
            max={localEnd}
          />
        </div>
        <div className="date-input-group">
          <label>End Date</label>
          <input
            type="date"
            value={localEnd}
            onChange={(e) => setLocalEnd(e.target.value)}
            min={localStart}
            max={new Date().toISOString().split('T')[0]}
          />
        </div>
        <button onClick={handleApply} className="apply-btn">
          Apply
        </button>
      </div>

      <div className="preset-buttons">
        <button onClick={() => setPresetRange(7)} className="preset-btn">
          Last 7 Days
        </button>
        <button onClick={() => setPresetRange(30)} className="preset-btn">
          Last 30 Days
        </button>
        <button onClick={() => setPresetRange(90)} className="preset-btn">
          Last 90 Days
        </button>
      </div>
    </div>
  );
};

