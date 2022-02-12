package com.example.warehouseproject;

public class User {
    public String id;
    public String type;
    public String name;
    public String number;
    public String email;
    public String password;
    public String lokacija;
    public Double lat;
    public Double lon;
    public Double rating;
    public Integer numRatings;


    public User() {
    }

    public User(String type, String name, String number, String email, String password, String lokacija, Double lat, Double lon) {
        this.type = type;
        this.name = name;
        this.number = number;
        this.email = email;
        this.password = password;
        this.lokacija = lokacija;
        this.lat = lat;
        this.lon = lon;
        this.rating = 0.0;
        this.numRatings = 0;
    }

    public User(String type, String name, String number, String email, String password) {
        this.type = type;
        this.name = name;
        this.number = number;
        this.email = email;
        this.password = password;
        this.rating = 0.0;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(Integer numRatings) {
        this.numRatings = numRatings;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", lokacija='" + lokacija + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", rating=" + rating +
                '}';
    }
}
