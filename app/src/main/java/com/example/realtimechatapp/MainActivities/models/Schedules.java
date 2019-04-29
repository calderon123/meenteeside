package com.example.realtimechatapp.MainActivities.models;

public class Schedules {

    private String mentee_sched_id;
    private String counselor_sched_id;
    private String date_sched;
    private String set_dscrpt;


    public Schedules(){

    }
    public Schedules(String mentee_sched_id, String counselor_sched_id, String date_sched, String set_dscrpt) {
        this.mentee_sched_id = mentee_sched_id;
        this.counselor_sched_id = counselor_sched_id;
        this.date_sched = date_sched;
        this.set_dscrpt = set_dscrpt;
    }

    public String getMentee_sched_id() {
        return mentee_sched_id;
    }

    public void setMentee_sched_id(String mentee_sched_id) {
        this.mentee_sched_id = mentee_sched_id;
    }

    public String getCounselor_sched_id() {
        return counselor_sched_id;
    }

    public void setCounselor_sched_id(String counselor_sched_id) {
        this.counselor_sched_id = counselor_sched_id;
    }

    public String getDate_sched() {
        return date_sched;
    }

    public void setDate_sched(String date_sched) {
        this.date_sched = date_sched;
    }

    public String getSet_dscrpt() {
        return set_dscrpt;
    }

    public void setSet_dscrpt(String set_dscrpt) {
        this.set_dscrpt = set_dscrpt;
    }
}
