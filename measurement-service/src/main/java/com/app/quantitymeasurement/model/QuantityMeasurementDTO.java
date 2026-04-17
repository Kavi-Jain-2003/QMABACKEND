package com.app.quantitymeasurement.model;

public class QuantityMeasurementDTO {
    private QuantityDTO thisQuantityDTO;
    private QuantityDTO thatQuantityDTO;

    public QuantityDTO getThisQuantityDTO(){ return thisQuantityDTO; }

    public void setThisQuantityDTO(QuantityDTO thisQuantityDTO){ this.thisQuantityDTO = thisQuantityDTO; }

    public QuantityDTO getThatQuantityDTO(){ return thatQuantityDTO; }

    public void setThatQuantityDTO(QuantityDTO thatQuantityDTO){ this.thatQuantityDTO = thatQuantityDTO; }
}