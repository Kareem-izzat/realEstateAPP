package com.example.realestate.models;

public class ReservedProperty {
    private int reservationId; // ADD THIS
    private Property property;
    private String reservationDate;

    public ReservedProperty(int reservationId, Property property, String reservationDate) {
        this.reservationId = reservationId;
        this.property = property;
        this.reservationDate = reservationDate;
    }

    public int getReservationId() {
        return reservationId;
    }

    public Property getProperty() {
        return property;
    }

    public String getReservationDate() {
        return reservationDate;
    }
}
