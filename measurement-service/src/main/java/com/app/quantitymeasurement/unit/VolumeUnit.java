package com.app.quantitymeasurement.unit;

public enum VolumeUnit implements IMeasurable {
    LITRE(1.0),
    MILLILITRE(0.001),
    CUBIC_METRE(1000.0),
    CUBIC_CENTIMETRE(0.001),
    GALLON(3.78541),
    QUART(0.946353),
    PINT(0.473176),
    FLUID_OUNCE(0.0295735),
    CUP(0.236588);

    private final double conversionFactor;
    private static final SupportsArithmetic sm = () -> true;

    private VolumeUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double convertToBase(double value) {
        return value * conversionFactor;
    }

    @Override
    public double convertFromBase(double value) {
        return value / conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public String getUnitName() {
        return this.name();
    }
}
