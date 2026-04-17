package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.unit.*;
import java.util.Map;

public class QuantityModel {

    private static final Map<String, String> UNIT_ALIASES = Map.ofEntries(
            Map.entry("METER", "METRE"),
            Map.entry("METERS", "METRE"),
            Map.entry("METRE", "METRE"),
            Map.entry("METRES", "METRE"),

            Map.entry("KILOMETER", "KILOMETRE"),
            Map.entry("KILOMETERS", "KILOMETRE"),
            Map.entry("KILOMETRE", "KILOMETRE"),
            Map.entry("KILOMETRES", "KILOMETRE"),

            Map.entry("CENTIMETER", "CENTIMETRE"),
            Map.entry("CENTIMETERS", "CENTIMETRE"),
            Map.entry("CENTIMETRE", "CENTIMETRE"),
            Map.entry("CENTIMETRES", "CENTIMETRE"),

            Map.entry("MILLIMETER", "MILLIMETRE"),
            Map.entry("MILLIMETERS", "MILLIMETRE"),
            Map.entry("MILLIMETRE", "MILLIMETRE"),
            Map.entry("MILLIMETRES", "MILLIMETRE"),

            Map.entry("FOOT", "FEET"),
            Map.entry("FEET", "FEET"),
            Map.entry("INCHES", "INCH"),
            Map.entry("INCH", "INCH"),

            Map.entry("LITER", "LITRE"),
            Map.entry("LITERS", "LITRE"),
            Map.entry("LITRE", "LITRE"),
            Map.entry("LITRES", "LITRE"),

            Map.entry("MILLILITER", "MILLILITRE"),
            Map.entry("MILLILITERS", "MILLILITRE"),
            Map.entry("MILLILITRE", "MILLILITRE"),
            Map.entry("MILLILITRES", "MILLILITRE")
    );

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
        String normalized = unit.trim().toUpperCase();

        // Accept display labels like "Metre (m)" or "Foot" from the UI.
        normalized = normalized.replaceAll("\\s*\\([^)]*\\)", "");
        normalized = normalized.replaceAll("[^A-Z0-9]+", "_");
        normalized = normalized.replaceAll("_+", "_");
        normalized = normalized.replaceAll("^_|_$", "");

        return UNIT_ALIASES.getOrDefault(normalized, normalized);
    }
}
