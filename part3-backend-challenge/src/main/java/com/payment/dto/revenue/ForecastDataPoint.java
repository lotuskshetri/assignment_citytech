package com.payment.dto.revenue;

import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;

@Serdeable
public class ForecastDataPoint {
    private String period;
    private BigDecimal predictedRevenue;
    private BigDecimal lowerBound;
    private BigDecimal upperBound;

    public ForecastDataPoint() {
    }

    public ForecastDataPoint(String period, BigDecimal predictedRevenue) {
        this.period = period;
        this.predictedRevenue = predictedRevenue;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getPredictedRevenue() {
        return predictedRevenue;
    }

    public void setPredictedRevenue(BigDecimal predictedRevenue) {
        this.predictedRevenue = predictedRevenue;
    }

    public BigDecimal getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(BigDecimal lowerBound) {
        this.lowerBound = lowerBound;
    }

    public BigDecimal getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(BigDecimal upperBound) {
        this.upperBound = upperBound;
    }
}

