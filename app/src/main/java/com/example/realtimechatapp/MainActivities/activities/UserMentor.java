package com.example.realtimechatapp.MainActivities.activities;

public class UserMentor {

    private Long id;

    public UserMentor(Long id) {
        this.id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    private String Image;
    private String fullname;
    private String expertise;
    private String rate;
    private String availability;
    private String email;
    private String date_reg;

    public UserMentor(Long id, String fullname, String expertise, String rate, String availability, String date_reg, String email) {
        this.id = id;
        this.fullname = fullname;
        this.expertise = expertise;
        this.rate = rate;
        this.availability = availability;
        this.date_reg = date_reg;
        this.email = email;
    }
    public UserMentor(){

    }

    public String getDate_reg() {
        return date_reg;
    }

    public void setDate_reg(String date_reg) {
        this.date_reg = date_reg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
