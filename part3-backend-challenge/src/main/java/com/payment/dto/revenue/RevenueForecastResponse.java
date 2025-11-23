package com.payment.dto.revenue;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public class RevenueForecastResponse {
    private List<ForecastDataPoint> forecast;
    private List<PeriodRevenue> historicalData;
    private String method; // "linear_regression" or "moving_average"
    private Double confidence;

    public RevenueForecastResponse() {
    }

    public List<ForecastDataPoint> getForecast() {
        return forecast;
    }

    public void setForecast(List<ForecastDataPoint> forecast) {
        this.forecast = forecast;
    }

    public List<PeriodRevenue> getHistoricalData() {
        return historicalData;
    }

    public void setHistoricalData(List<PeriodRevenue> historicalData) {
        this.historicalData = historicalData;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}

