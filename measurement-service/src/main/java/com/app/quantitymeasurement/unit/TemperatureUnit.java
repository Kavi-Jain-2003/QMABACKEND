package com.app.quantitymeasurement.unit;

public enum TemperatureUnit implements IMeasurable {

    CELSIUS {
        public double convertToBase(double value) { return value; }
        public double convertFromBase(double value) { return value; }
    },

    FAHRENHEIT {
        public double convertToBase(double value) { return (value - 32) * 5 / 9; }
        public double convertFromBase(double value) { return (value * 9 / 5) + 32; }
    },

    KELVIN {
        public double convertToBase(double value) { return value - 273.15; }
        public double convertFromBase(double value) { return value + 273.15; }
    },

    RANKINE {
        public double convertToBase(double value) { return (value - 491.67) * 5 / 9; }
        public double convertFromBase(double value) { return (value + 273.15) * 9 / 5; }
    };

    private static final SupportsArithmetic supportsArithmetic = () -> false;

    @Override
    public double getConversionFactor() {
        return 1.0;
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public void validOperationSupport(String operation) {
        if (!supportsArithmetic.isSupported()) {
            throw new UnsupportedOperationException(
                    "Temperature does not support " + operation
            );
        }
    }
}
