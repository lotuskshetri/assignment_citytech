package com.payment.dto.analytics;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class HeatmapCell {
    private Integer hour; // 0-23
    private Integer dayOfWeek; // 0-6 (0 = Sunday)
    private Long transactionCount;

    public HeatmapCell() {
    }

    public HeatmapCell(Integer hour, Integer dayOfWeek, Long transactionCount) {
        this.hour = hour;
        this.dayOfWeek = dayOfWeek;
        this.transactionCount = transactionCount;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Long transactionCount) {
        this.transactionCount = transactionCount;
    }
}

