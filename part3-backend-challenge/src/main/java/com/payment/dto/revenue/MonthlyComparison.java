package com.payment.dto.revenue;

import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;

@Serdeable
public class MonthlyComparison {
    private Integer month;
    private String monthName;
    private BigDecimal currentYearRevenue;
    private BigDecimal previousYearRevenue;
    private Double growthRate;

    public MonthlyComparison() {
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public BigDecimal getCurrentYearRevenue() {
        return currentYearRevenue;
    }

    public void setCurrentYearRevenue(BigDecimal currentYearRevenue) {
        this.currentYearRevenue = currentYearRevenue;
    }

    public BigDecimal getPreviousYearRevenue() {
        return previousYearRevenue;
    }

    public void setPreviousYearRevenue(BigDecimal previousYearRevenue) {
        this.previousYearRevenue = previousYearRevenue;
    }

    public Double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(Double growthRate) {
        this.growthRate = growthRate;
    }
}

