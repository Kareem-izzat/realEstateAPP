package com.example.realestate.models;

public class ReservedProperty {
    private Property property;
    private String reservationDate;

    public ReservedProperty(Property property, String reservationDate) {
        this.property = property;
        this.reservationDate = reservationDate;
    }

    public Property getProperty() {
        return property;
    }

    public String getReservationDate() {
        return reservationDate;
    }
}

