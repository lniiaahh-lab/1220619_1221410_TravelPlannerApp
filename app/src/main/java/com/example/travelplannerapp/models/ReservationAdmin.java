package com.example.travelplannerapp.models;

public class ReservationAdmin {

    private String userEmail;
    private String destination;
    private String date;
    private String status;
    private int quantity;
    private String type;

    public ReservationAdmin(
            String userEmail,
            String destination,
            String date,
            String status,
            int quantity,
            String type) {

        this.userEmail = userEmail;
        this.destination = destination;
        this.date = date;
        this.status = status;
        this.quantity = quantity;
        this.type = type;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getType() {
        return type;
    }
}