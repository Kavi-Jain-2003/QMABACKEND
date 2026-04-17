package com.app.quantitymeasurement.model;
public class QuantityDTO {

    private double value;
    private String unit;

    public QuantityDTO() {}   // REQUIRED

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}

