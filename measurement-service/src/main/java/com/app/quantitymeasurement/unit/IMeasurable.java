package com.app.quantitymeasurement.unit;

@FunctionalInterface
interface SupportsArithmetic {
    boolean isSupported();
}

public interface IMeasurable {
    double getConversionFactor();

    double convertToBase(double value);

    double convertFromBase(double value);
    
    String getUnitName();

    default void validOperationSupport(String operation){}
}