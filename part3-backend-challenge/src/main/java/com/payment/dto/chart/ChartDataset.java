package com.payment.dto.chart;

import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public class ChartDataset {
    private String label;
    private List<Object> data;
    private String backgroundColor;
    private String borderColor;
    private Integer borderWidth;
    private Boolean fill;

    public ChartDataset() {
    }

    public ChartDataset(String label, List<Object> data) {
        this.label = label;
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public Integer getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(Integer borderWidth) {
        this.borderWidth = borderWidth;
    }

    public Boolean getFill() {
        return fill;
    }

    public void setFill(Boolean fill) {
        this.fill = fill;
    }
}

