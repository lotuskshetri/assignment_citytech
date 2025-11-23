package com.payment.dto.revenue;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public class TopPerformersResponse {
    private List<MerchantRevenueData> topMerchants;
    private String sortedBy;
    private String dateRange;

    public TopPerformersResponse() {
    }

    public List<MerchantRevenueData> getTopMerchants() {
        return topMerchants;
    }

    public void setTopMerchants(List<MerchantRevenueData> topMerchants) {
        this.topMerchants = topMerchants;
    }

    public String getSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(String sortedBy) {
        this.sortedBy = sortedBy;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }
}

