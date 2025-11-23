package com.payment.dto.analytics;

import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;

@Serdeable
public class VolumeDataPoint {
    private String period;
    private Long transactionCount;
    private BigDecimal totalAmount;
    private BigDecimal averageAmount;

    public VolumeDataPoint() {
    }

    public VolumeDataPoint(String period, Long transactionCount, BigDecimal totalAmount, BigDecimal averageAmount) {
        this.period = period;
        this.transactionCount = transactionCount;
        this.totalAmount = totalAmount;
        this.averageAmount = averageAmount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAverageAmount() {
        return averageAmount;
    }

    public void setAverageAmount(BigDecimal averageAmount) {
        this.averageAmount = averageAmount;
    }
}

