package com.example.realestate.models;

public class Reservation {
    private int id;
    private String userEmail;
    private int propertyId;
    private String reservationDate;
    private String notes;
    private String startDate;
    private String endDate;

    public Reservation(int id, String userEmail, int propertyId, String startDate, String endDate, String notes) {
        this.id = id;
        this.userEmail = userEmail;
        this.propertyId = propertyId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
    }

    public Reservation(int id, String userEmail, int propertyId, String startDate, String endDate) {
        this.id = id;
        this.userEmail = userEmail;
        this.propertyId = propertyId;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    // Getters
    public int getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public int getPropertyId() { return propertyId; }
    public String getReservationDate() { return reservationDate; }
    public String getNotes() { return notes; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public void setPropertyId(int propertyId) { this.propertyId = propertyId; }
    public void setReservationDate(String reservationDate) { this.reservationDate = reservationDate; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", userEmail='" + userEmail + '\'' +
                ", propertyId=" + propertyId +
                ", reservationDate='" + reservationDate + '\'' +
                ", notes='" + notes + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
