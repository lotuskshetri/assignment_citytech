package com.payment.dto.revenue;

import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;
import java.util.List;

@Serdeable
public class RevenueByMerchantResponse {
    private List<MerchantRevenueData> merchants;
    private BigDecimal totalRevenue;
    private Integer totalMerchants;

    public RevenueByMerchantResponse() {
    }

    public List<MerchantRevenueData> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<MerchantRevenueData> merchants) {
        this.merchants = merchants;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getTotalMerchants() {
        return totalMerchants;
    }

    public void setTotalMerchants(Integer totalMerchants) {
        this.totalMerchants = totalMerchants;
    }
}

