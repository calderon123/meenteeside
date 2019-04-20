package com.example.realtimechatapp.MainActivities.models;

public class Counselors {

    private String id;
    private  String imageURL;
    private String email;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Counselors(String id) {
        this.id = id;
    }

    private  String fullname;
    private  String rate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private  String status;
    private  String expertise;
    private  String availability;

    public Counselors(String id, String fullname, String rate, String expertise, String availability, String email) {
        this.id = id;
        this.fullname = fullname;
        this.rate = rate;
        this.expertise = expertise;
        this.availability = availability;
    }
    public  Counselors(){

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
