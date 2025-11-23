package com.payment.dto.revenue;

import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;
import java.util.List;

@Serdeable
public class RevenueByPeriodResponse {
    private List<PeriodRevenue> periods;
    private BigDecimal totalRevenue;
    private Long totalTransactions;
    private String groupBy;

    public RevenueByPeriodResponse() {
    }

    public List<PeriodRevenue> getPeriods() {
        return periods;
    }

    public void setPeriods(List<PeriodRevenue> periods) {
        this.periods = periods;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }
}

