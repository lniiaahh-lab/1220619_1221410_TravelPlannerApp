package com.example.travelplannerapp.models;

public class Reservation {

    private String destination;
    private String reservationDate;
    private String status;
    private int quantity;
    private String type;

    public Reservation(String destination,
                       String reservationDate,
                       String status,
                       int quantity,
                       String type) {

        this.destination = destination;
        this.reservationDate = reservationDate;
        this.status = status;
        this.quantity = quantity;
        this.type = type;
    }

    public String getDestination() {
        return destination;
    }

    public String getReservationDate() {
        return reservationDate;
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

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setType(String type) {
        this.type = type;
    }
}