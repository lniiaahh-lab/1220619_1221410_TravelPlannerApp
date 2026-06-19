package com.example.travelplannerapp.models;

public class Trip {

    private int id;
    private String destination;
    private String country;
    private int durationDays;
    private double price;
    private double rating;
    private String description;
    private String image;

    public Trip() {
    }



    public Trip(int id, String destination, String country,
                int durationDays, double price,
                double rating, String description, String image) {

        this.id = id;
        this.destination = destination;
        this.country = country;
        this.durationDays = durationDays;
        this.price = price;
        this.rating = rating;
        this.description = description;
        this.image = image;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getDestination() {

        return destination;
    }

    public String getCountry() {

        return country;
    }

    public int getDurationDays() {

        return durationDays;
    }

    public double getPrice() {

        return price;
    }

    public double getRating() {

        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}