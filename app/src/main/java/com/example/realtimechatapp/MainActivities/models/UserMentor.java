package com.example.realtimechatapp.MainActivities.models;
public class UserMentor {

    private String id;
    private String status;
    private String imageURL;
    private String fullname;
    private String expertise;
    private String rate;
    private String availability;
    private String email;
    private String date_reg;
    private String handles;
    private String search;


    public  UserMentor(){

    }

    public UserMentor(String id, String status, String imageURL, String fullname, String expertise, String rate, String availability, String email, String date_reg, String handles, String search) {
        this.id = id;
        this.status = status;
        this.imageURL = imageURL;
        this.fullname = fullname;
        this.expertise = expertise;
        this.rate = rate;
        this.availability = availability;
        this.email = email;
        this.date_reg = date_reg;
        this.handles = handles;
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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

    public String getDate_reg() {
        return date_reg;
    }

    public void setDate_reg(String date_reg) {
        this.date_reg = date_reg;
    }

    public String getHandles() {
        return handles;
    }

    public void setHandles(String handles) {
        this.handles = handles;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}