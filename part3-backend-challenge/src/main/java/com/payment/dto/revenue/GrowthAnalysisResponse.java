package com.payment.dto.revenue;

import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;
import java.util.List;

@Serdeable
public class GrowthAnalysisResponse {
    private Integer currentYear;
    private Integer comparisonYear;
    private Double overallGrowthRate;
    private BigDecimal currentYearTotal;
    private BigDecimal comparisonYearTotal;
    private List<MonthlyComparison> monthlyComparison;

    public GrowthAnalysisResponse() {
    }

    public Integer getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(Integer currentYear) {
        this.currentYear = currentYear;
    }

    public Integer getComparisonYear() {
        return comparisonYear;
    }

    public void setComparisonYear(Integer comparisonYear) {
        this.comparisonYear = comparisonYear;
    }

    public Double getOverallGrowthRate() {
        return overallGrowthRate;
    }

    public void setOverallGrowthRate(Double overallGrowthRate) {
        this.overallGrowthRate = overallGrowthRate;
    }

    public BigDecimal getCurrentYearTotal() {
        return currentYearTotal;
    }

    public void setCurrentYearTotal(BigDecimal currentYearTotal) {
        this.currentYearTotal = currentYearTotal;
    }

    public BigDecimal getComparisonYearTotal() {
        return comparisonYearTotal;
    }

    public void setComparisonYearTotal(BigDecimal comparisonYearTotal) {
        this.comparisonYearTotal = comparisonYearTotal;
    }

    public List<MonthlyComparison> getMonthlyComparison() {
        return monthlyComparison;
    }

    public void setMonthlyComparison(List<MonthlyComparison> monthlyComparison) {
        this.monthlyComparison = monthlyComparison;
    }
}

