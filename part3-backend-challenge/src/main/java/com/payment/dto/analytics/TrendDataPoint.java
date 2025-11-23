package com.payment.dto.analytics;

import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;

@Serdeable
public class TrendDataPoint {
    private String period;
    private BigDecimal averageAmount;
    private Long transactionCount;
    private Double changePercentage;

    public TrendDataPoint() {
    }

    public TrendDataPoint(String period, BigDecimal averageAmount, Long transactionCount) {
        this.period = period;
        this.averageAmount = averageAmount;
        this.transactionCount = transactionCount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getAverageAmount() {
        return averageAmount;
    }

    public void setAverageAmount(BigDecimal averageAmount) {
        this.averageAmount = averageAmount;
    }

    public Long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Double getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(Double changePercentage) {
        this.changePercentage = changePercentage;
    }
}

