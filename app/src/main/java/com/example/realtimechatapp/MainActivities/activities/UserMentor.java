package com.example.realtimechatapp.MainActivities.activities;

public class UserMentor {

    private String id;
    private String fullname;
    private String expertise;
    private String rate;
    private String availability;
    private String email;

    public UserMentor(String id, String fullname, String expertise, String rate, String availability, String email) {
        this.id = id;
        this.fullname = fullname;
        this.expertise = expertise;
        this.rate = rate;
        this.availability = availability;
        this.email = email;
    }
    public UserMentor(){

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

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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
