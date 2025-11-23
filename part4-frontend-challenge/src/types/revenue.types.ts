// Revenue Report Type Definitions

export interface PeriodRevenue {
  period: string;
  revenue: number;
  transactionCount: number;
  averageTransaction: number;
}

export interface RevenueByPeriodResponse {
  periods: PeriodRevenue[];
  totalRevenue: number;
  totalTransactions: number;
  groupBy: string;
}

export interface MerchantRevenue {
  merchantId: string;
  revenue: number;
  transactionCount: number;
  averageTransaction: number;
  percentageOfTotal: number;
  rank: number;
}

export interface RevenueByMerchantResponse {
  merchants: MerchantRevenue[];
  totalRevenue: number;
  totalMerchants: number;
}

export interface ForecastPeriod {
  period: string;
  predictedRevenue: number;
  lowerBound: number;
  upperBound: number;
}

export interface RevenueForecastResponse {
  forecast: ForecastPeriod[];
  historicalData: PeriodRevenue[];
  method: string;
  confidence: number;
}

export interface MonthlyComparison {
  month: number;
  monthName: string;
  currentYearRevenue: number;
  previousYearRevenue: number;
  growthRate: number;
}

export interface GrowthAnalysisResponse {
  currentYear: number;
  comparisonYear: number;
  overallGrowthRate: number;
  currentYearTotal: number;
  comparisonYearTotal: number;
  monthlyComparison: MonthlyComparison[];
}

export interface TopPerformersResponse {
  topMerchants: MerchantRevenue[];
  sortedBy: string;
  dateRange: string;
}

