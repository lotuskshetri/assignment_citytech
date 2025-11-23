package com.payment.dto.revenue;

import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;

@Serdeable
public class PeriodRevenue {
    private String period;
    private BigDecimal revenue;
    private Long transactionCount;
    private BigDecimal averageTransaction;

    public PeriodRevenue() {
    }

    public PeriodRevenue(String period, BigDecimal revenue, Long transactionCount, BigDecimal averageTransaction) {
        this.period = period;
        this.revenue = revenue;
        this.transactionCount = transactionCount;
        this.averageTransaction = averageTransaction;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public Long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public BigDecimal getAverageTransaction() {
        return averageTransaction;
    }

    public void setAverageTransaction(BigDecimal averageTransaction) {
        this.averageTransaction = averageTransaction;
    }
}

