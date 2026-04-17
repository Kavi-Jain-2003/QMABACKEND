package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.unit.Quantity;

public interface IQuantityMeasurementService {

    QuantityMeasurementEntity compare(Quantity<?> quantity1, Quantity<?> quantity2);
    QuantityMeasurementEntity convert(Quantity<?> quantity, Quantity<?> targetQuantity);
    QuantityMeasurementEntity add(Quantity<?> quantity1, Quantity<?> quantity2);
    QuantityMeasurementEntity subtract(Quantity<?> quantity1, Quantity<?> quantity2);
    QuantityMeasurementEntity multiply(Quantity<?> q1, Quantity<?> q2);

    QuantityMeasurementEntity divide(Quantity<?> quantity1, Quantity<?> quantity2);
}
