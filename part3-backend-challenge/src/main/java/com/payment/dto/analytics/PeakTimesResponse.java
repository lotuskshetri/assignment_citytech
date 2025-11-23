package com.payment.dto.analytics;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public class PeakTimesResponse {
    private List<HeatmapCell> heatmapData;
    private Integer busiestHour;
    private Integer busiestDay;
    private Long peakTransactionCount;

    public PeakTimesResponse() {
    }

    public PeakTimesResponse(List<HeatmapCell> heatmapData) {
        this.heatmapData = heatmapData;
    }

    public List<HeatmapCell> getHeatmapData() {
        return heatmapData;
    }

    public void setHeatmapData(List<HeatmapCell> heatmapData) {
        this.heatmapData = heatmapData;
    }

    public Integer getBusiestHour() {
        return busiestHour;
    }

    public void setBusiestHour(Integer busiestHour) {
        this.busiestHour = busiestHour;
    }

    public Integer getBusiestDay() {
        return busiestDay;
    }

    public void setBusiestDay(Integer busiestDay) {
        this.busiestDay = busiestDay;
    }

    public Long getPeakTransactionCount() {
        return peakTransactionCount;
    }

    public void setPeakTransactionCount(Long peakTransactionCount) {
        this.peakTransactionCount = peakTransactionCount;
    }
}

