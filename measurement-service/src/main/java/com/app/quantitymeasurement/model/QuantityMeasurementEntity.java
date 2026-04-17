package com.app.quantitymeasurement.model;

import java.time.LocalDateTime;

public class QuantityMeasurementEntity {

    private Long id;
    private String operation, operand1, operand2, result, error;
    private LocalDateTime createdAt;

    public QuantityMeasurementEntity(){}

    public QuantityMeasurementEntity(String operation, String operand1, String operand2,
                                     String result) {
        this.operation = operation;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
    }

    public QuantityMeasurementEntity(String errorMessage) {
        this.error = errorMessage;
    }

    public void PrePersist(){ createdAt = LocalDateTime.now(); }

    public boolean hasError(){ return error != null; }

    public String getOperation(){return operation; }

    public String getOperand1(){ return operand1; } 

    public String getOperand2(){ return operand2;}

    public String getResult(){return result;}

    public String getError(){ return error;}
}