// Analytics Type Definitions

export interface TransactionVolume {
  totalTransactions: number;
  totalAmount: number;
  averageAmount: number;
  period: string;
}

export interface SuccessRate {
  totalTransactions: number;
  completedCount: number;
  pendingCount: number;
  failedCount: number;
  reversedCount: number;
  successRate: number;
  statusBreakdown?: Record<string, number>;
}

export interface TrendData {
  date: string;
  transactionCount: number;
  totalAmount: number;
  successRate: number;
}

export interface TransactionTrends {
  trends: TrendData[];
  period: string;
  startDate: string;
  endDate: string;
}

export interface HourlyData {
  hour: number;
  transactionCount: number;
  averageAmount: number;
}

export interface DailyData {
  dayOfWeek: string;
  transactionCount: number;
  averageAmount: number;
}

export interface PeakTimesHeatmap {
  hourlyData: HourlyData[];
  dailyData: DailyData[];
  peakHour: number;
  peakDay: string;
}

export interface CardTypeData {
  cardType: string;
  count: number;
  percentage: number;
  totalAmount: number;
}

export interface CardDistribution {
  distribution: CardTypeData[];
  totalTransactions: number;
  period: string;
}

export interface DateRangeParams {
  startDate: string; // YYYY-MM-DD
  endDate: string;   // YYYY-MM-DD
}

