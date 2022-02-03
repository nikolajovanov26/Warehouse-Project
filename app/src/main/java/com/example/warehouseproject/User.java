package com.example.warehouseproject;

public class User {
    public String type;
    public String name;
    public String number;
    public String email;
    public String password;
    public String lokacija;
    public Double lat;
    public Double lon;
    public Double rating;


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
    }

    public User(String type, String name, String number, String email, String password) {
        this.type = type;
        this.name = name;
        this.number = number;
        this.email = email;
        this.password = password;
        this.rating = 0.0;
    }

}
