package com.payment.dto.analytics;

import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;

@Serdeable
public class CardTypeData {
    private String cardType;
    private Long count;
    private Double percentage;
    private BigDecimal totalAmount;

    public CardTypeData() {
    }

    public CardTypeData(String cardType, Long count, BigDecimal totalAmount) {
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

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}

