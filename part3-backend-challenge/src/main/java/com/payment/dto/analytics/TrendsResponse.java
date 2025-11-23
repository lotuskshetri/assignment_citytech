package com.payment.dto.analytics;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public class TrendsResponse {
    private List<TrendDataPoint> trends;
    private String trendDirection; // "up", "down", "stable"
    private Double overallChangePercentage;

    public TrendsResponse() {
    }

    public TrendsResponse(List<TrendDataPoint> trends) {
        this.trends = trends;
    }

    public List<TrendDataPoint> getTrends() {
        return trends;
    }

    public void setTrends(List<TrendDataPoint> trends) {
        this.trends = trends;
    }

    public String getTrendDirection() {
        return trendDirection;
    }

    public void setTrendDirection(String trendDirection) {
        this.trendDirection = trendDirection;
    }

    public Double getOverallChangePercentage() {
        return overallChangePercentage;
    }

    public void setOverallChangePercentage(Double overallChangePercentage) {
        this.overallChangePercentage = overallChangePercentage;
    }
}

