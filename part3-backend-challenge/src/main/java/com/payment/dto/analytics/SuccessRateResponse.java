package com.payment.dto.analytics;

import io.micronaut.serde.annotation.Serdeable;
import java.util.Map;

@Serdeable
public class SuccessRateResponse {
    private Long totalTransactions;
    private Long completedCount;
    private Long pendingCount;
    private Long failedCount;
    private Long reversedCount;
    private Double successRate;
    private Map<String, Long> statusBreakdown;

    public SuccessRateResponse() {
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public Long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(Long completedCount) {
        this.completedCount = completedCount;
    }

    public Long getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(Long pendingCount) {
        this.pendingCount = pendingCount;
    }

    public Long getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Long failedCount) {
        this.failedCount = failedCount;
    }

    public Long getReversedCount() {
        return reversedCount;
    }

    public void setReversedCount(Long reversedCount) {
        this.reversedCount = reversedCount;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    public Map<String, Long> getStatusBreakdown() {
        return statusBreakdown;
    }

    public void setStatusBreakdown(Map<String, Long> statusBreakdown) {
        this.statusBreakdown = statusBreakdown;
    }
}

