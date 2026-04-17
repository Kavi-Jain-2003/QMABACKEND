package com.app.quantitymeasurement.unit;

public enum LengthUnit implements IMeasurable {
    METRE(1.0),
    KILOMETRE(1000.0),
    CENTIMETRE(0.01),
    MILLIMETRE(0.001),
    MILE(1609.344),
    YARD(0.9144),
    FEET(0.3048),
    INCH(0.0254),
    NAUTICAL_MILE(1852.0);

    private final double toMetre;
    private static final SupportsArithmetic sm = () -> true;

    LengthUnit(double toMetre) {
        this.toMetre = toMetre;
    }

    @Override
    public double convertToBase(double value) {
        return value * toMetre;
    }

    @Override
    public double convertFromBase(double value) {
        return value / toMetre;
    }

    @Override
    public double getConversionFactor() {
        return toMetre;
    }

    @Override
    public String getUnitName() {
        return this.name();
    }
}
