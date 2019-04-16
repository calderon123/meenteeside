package com.example.realtimechatapp.MainActivities.models;

public class Mentees {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String id;
    private  String imageurl;

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Mentees(String id) {
        this.id = id;
    }

    private  String fullname;
    private  String rate;

    public String getStatus_counselor() {
        return status_counselor;
    }

    public void setStatus_counselor(String status_counselor) {
        this.status_counselor = status_counselor;
    }

    private  String status_counselor;
    private  String expertise;
    private  String availability;

    public Mentees(String id, String fullname, String rate, String expertise, String availability) {
        this.id = id;
        this.fullname = fullname;
        this.rate = rate;
        this.expertise = expertise;
        this.availability = availability;
    }
    public Mentees(){

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
}
