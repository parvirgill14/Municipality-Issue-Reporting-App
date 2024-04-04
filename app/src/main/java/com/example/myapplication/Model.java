package com.example.myapplication;

import com.google.firebase.database.ServerValue;

public class Model {
    private String imageURL, title, description, user;
    private double latitude,longitude, order;
    private int votes;
    private Object timestamp;

    public Model(){

    }
    public Model(String title, String description, String imageURL, double latitude, double longitude, String user, int votes, double order){
        this.title = title;
        this.description = description;
        this.imageURL= imageURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.votes = votes;
        this.user = user;
        this.timestamp = ServerValue.TIMESTAMP;
        this.order = order;
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

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getOrder() {
        return order;
    }

    public void setOrder(double order) {
        this.order = order;
    }
}
