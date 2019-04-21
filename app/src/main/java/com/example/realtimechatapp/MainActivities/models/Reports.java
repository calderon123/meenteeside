package com.example.realtimechatapp.MainActivities.models;

public class Reports {

    private String report_type;
    private String id;
    private String imageURL;
    private String report_dscrpt;

    public Reports(String report_type, String id, String imageURL, String report_dscrpt) {
        this.report_type = report_type;
        this.id = id;
        this.imageURL = imageURL;
        this.report_dscrpt = report_dscrpt;
    }
    public Reports(){

    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getReport_dscrpt() {
        return report_dscrpt;
    }

    public void setReport_dscrpt(String report_dscrpt) {
        this.report_dscrpt = report_dscrpt;
    }
}
