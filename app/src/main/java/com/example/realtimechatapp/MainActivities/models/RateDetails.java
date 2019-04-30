package com.example.realtimechatapp.MainActivities.models;

import android.content.Context;

public class RateDetails {

    private String rate;
    private String comments;
    private String id;
    private  String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RateDetails(String date) {
        this.date = date;
    }

    public RateDetails(){

    }

    public RateDetails(String rate, String comments, String id) {
        this.rate = rate;
        this.comments = comments;
        this.id = id;
    }

    public String getRate() {
        return rate;
    }

    public void getRate(String rate) {
        this.rate = rate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
