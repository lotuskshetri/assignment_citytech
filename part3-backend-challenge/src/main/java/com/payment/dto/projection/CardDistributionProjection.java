package com.payment.dto.projection;

import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;

@Introspected
public class CardDistributionProjection {
    private String cardType;
    private Long count;
    private BigDecimal totalAmount;

    public CardDistributionProjection() {
    }

    public CardDistributionProjection(String cardType, Long count, BigDecimal totalAmount) {
        this.cardType = cardType;
        this.count = count;
        this.totalAmount = totalAmount;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}

