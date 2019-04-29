package com.example.realtimechatapp.MainActivities.models;

public class Chat {

    private boolean isseen;
    private String sender;
    private String receiver;
    private String message;
    private String message_sent;

    public Chat(){

    }

    public Chat(boolean isseen, String sender, String receiver, String message, String message_sent) {
        this.isseen = isseen;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.message_sent = message_sent;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_sent() {
        return message_sent;
    }

    public void setMessage_sent(String message_sent) {
        this.message_sent = message_sent;
    }
}

