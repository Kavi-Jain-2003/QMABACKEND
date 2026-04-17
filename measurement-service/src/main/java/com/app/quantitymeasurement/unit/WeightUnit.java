package com.app.quantitymeasurement.unit;

public enum WeightUnit implements IMeasurable {
    KILOGRAM(1.0),
    GRAM(0.001),
    MILLIGRAM(0.000001),
    POUND(0.45359237),
    OUNCE(0.028349523),
    TON(1000.0),
    STONE(6.35029);

    private final double toKilogram;
    private static final SupportsArithmetic sm = () -> true;

    WeightUnit(double toKilogram) {
        this.toKilogram = toKilogram;
    }

    @Override
    public double getConversionFactor() {
        return toKilogram;
    }

    @Override
    public double convertToBase(double baseValue) {
        return baseValue * toKilogram;
    }

    @Override
    public double convertFromBase(double baseValue) {
        return baseValue / toKilogram;
    }

    @Override
    public String getUnitName() {
        return this.name();
    }
}
