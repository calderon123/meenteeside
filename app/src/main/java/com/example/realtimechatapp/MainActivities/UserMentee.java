package com.example.realtimechatapp.MainActivities;

public class UserMentee  {


    private String user_id;
    private String fullname;
    private String email;
    private String password;



    public UserMentee(){

    }

    public UserMentee(String id,String fullname, String email,String password) {
        this.fullname = fullname;
        this.email = email;

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
