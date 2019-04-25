package com.example.realtimechatapp.MainActivities.models;

public class UserMentee  {


    private String imageURL;
    private String status;
    private String fullname;
    private String email;
    private String id;

    public UserMentee(){

    }

    public UserMentee(String imageURL, String status, String fullname, String email, String id) {
        this.imageURL = imageURL;
        this.status = status;
        this.fullname = fullname;
        this.email = email;
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
