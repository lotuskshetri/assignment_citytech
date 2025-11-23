package com.payment.dto.analytics;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public class CardDistributionResponse {
    private List<CardTypeData> distribution;
    private Long totalTransactions;

    public CardDistributionResponse() {
    }

    public CardDistributionResponse(List<CardTypeData> distribution, Long totalTransactions) {
        this.distribution = distribution;
        this.totalTransactions = totalTransactions;
    }

    public List<CardTypeData> getDistribution() {
        return distribution;
    }

    public void setDistribution(List<CardTypeData> distribution) {
        this.distribution = distribution;
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
}

