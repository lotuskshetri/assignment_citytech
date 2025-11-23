package com.payment.dto.chart;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public class ChartDataResponse {
    private List<String> labels;
    private List<ChartDataset> datasets;
    private String chartType; // "line", "bar", "pie"

    public ChartDataResponse() {
    }

    public ChartDataResponse(List<String> labels, List<ChartDataset> datasets, String chartType) {
        this.labels = labels;
        this.datasets = datasets;
        this.chartType = chartType;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<ChartDataset> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<ChartDataset> datasets) {
        this.datasets = datasets;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }
}

