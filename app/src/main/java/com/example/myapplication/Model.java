package com.example.myapplication;

public class Model {
    private String imageURL;
    private double latitude;
    private double longitude;
    private String time;

    public Model() {
        // Default constructor required for Firebase Realtime Database
    }

    public Model(String imageURL, double latitude, double longitude) {
        this.imageURL = imageURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
