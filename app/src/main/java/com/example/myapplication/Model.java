package com.example.myapplication;

public class Model {
    private String imageURL;
    private double latitude;
    private double longitude;
    private String title;
    private String description;
    public Model(){

    }
    public Model(String title, String description, String imageURL, double latitude, double longitude){
        this.title = title;
        this.description = description;
        this.imageURL= imageURL;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
