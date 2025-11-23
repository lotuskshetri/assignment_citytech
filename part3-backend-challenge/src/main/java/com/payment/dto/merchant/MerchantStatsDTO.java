package com.payment.dto.merchant;

import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;
import java.sql.Date;

@Serdeable
public class MerchantStatsDTO {
    @MappedProperty("merchant_id")
    private String merchantId;

    @MappedProperty("total_transactions")
    private Long totalTransactions;

    @MappedProperty("total_revenue")
    private BigDecimal totalRevenue;

    @MappedProperty("completed_count")
    private Long completedCount;

    @MappedProperty("failed_count")
    private Long failedCount;

    @MappedProperty("pending_count")
    private Long pendingCount;

    @MappedProperty("last_transaction_date")
    private Date lastTransactionDate;

    @MappedProperty("first_transaction_date")
    private Date firstTransactionDate;

    public MerchantStatsDTO() {
    }

    public MerchantStatsDTO(String merchantId, Long totalTransactions, BigDecimal totalRevenue,
                            Long completedCount, Long failedCount, Long pendingCount,
                            Date lastTransactionDate, Date firstTransactionDate) {
        this.merchantId = merchantId;
        this.totalTransactions = totalTransactions;
        this.totalRevenue = totalRevenue;
        this.completedCount = completedCount;
        this.failedCount = failedCount;
        this.pendingCount = pendingCount;
        this.lastTransactionDate = lastTransactionDate;
        this.firstTransactionDate = firstTransactionDate;
    }

    // Getters and Setters
    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(Long completedCount) {
        this.completedCount = completedCount;
    }

    public Long getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Long failedCount) {
        this.failedCount = failedCount;
    }

    public Long getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(Long pendingCount) {
        this.pendingCount = pendingCount;
    }

    public Date getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(Date lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public Date getFirstTransactionDate() {
        return firstTransactionDate;
    }

    public void setFirstTransactionDate(Date firstTransactionDate) {
        this.firstTransactionDate = firstTransactionDate;
    }
}

