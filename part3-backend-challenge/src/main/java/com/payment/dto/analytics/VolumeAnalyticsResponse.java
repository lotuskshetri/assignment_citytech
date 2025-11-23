package com.payment.dto.analytics;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public class VolumeAnalyticsResponse {
    private List<VolumeDataPoint> data;
    private String groupBy;
    private String dateRange;

    public VolumeAnalyticsResponse() {
    }

    public VolumeAnalyticsResponse(List<VolumeDataPoint> data, String groupBy, String dateRange) {
        this.data = data;
        this.groupBy = groupBy;
        this.dateRange = dateRange;
    }

    public List<VolumeDataPoint> getData() {
        return data;
    }

    public void setData(List<VolumeDataPoint> data) {
        this.data = data;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }
}

