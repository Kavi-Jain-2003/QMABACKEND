package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.unit.*;

public class QuantityModel {

    public static Quantity<?> toQuantity(QuantityDTO dto){
        if (dto == null || dto.getUnit() == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        String unit = normalize(dto.getUnit());

        switch (unit) {
            case "METRE":
            case "KILOMETRE":
            case "CENTIMETRE":
            case "MILLIMETRE":
            case "MILE":
            case "YARD":
            case "FEET":
            case "INCH":
            case "NAUTICAL_MILE":
                return new Quantity<>(dto.getValue(), LengthUnit.valueOf(unit));

            case "KILOGRAM":
            case "GRAM":
            case "MILLIGRAM":
            case "POUND":
            case "OUNCE":
            case "TON":
            case "STONE":
                return new Quantity<>(dto.getValue(), WeightUnit.valueOf(unit));

            case "LITRE":
            case "MILLILITRE":
            case "CUBIC_METRE":
            case "CUBIC_CENTIMETRE":
            case "GALLON":
            case "QUART":
            case "PINT":
            case "FLUID_OUNCE":
            case "CUP":
                return new Quantity<>(dto.getValue(), VolumeUnit.valueOf(unit));

            case "CELSIUS":
            case "FAHRENHEIT":
            case "KELVIN":
            case "RANKINE":
                return new Quantity<>(dto.getValue(), TemperatureUnit.valueOf(unit));

            default:
                throw new IllegalArgumentException("Invalid unit: " + dto.getUnit());
        }
    }

    private static String normalize(String unit) {
        return unit.trim()
                .toUpperCase()
                .replace(' ', '_')
                .replace('-', '_');
    }
}
