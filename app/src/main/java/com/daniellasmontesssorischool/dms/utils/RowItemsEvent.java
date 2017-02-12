package com.daniellasmontesssorischool.dms.utils;

/**
 * Created by Cahak on 25/04/2016.
 */
public class RowItemsEvent {

    private String participants;
    private String event_header;
    private String event_detail;
    private String event_month;
    private String event_day;
    private int event_col;


    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String part) {
        this.participants = part;
    }



    public String getEvent_header() {
        return event_header;
    }

    public void setEvent_header(String header) {
        this.event_header = header;
    }



    public String getEvent_detail() {return event_detail;}

    public void setEvent_detail(String detail) {
        this.event_detail = detail;
    }



    public String getEvent_month() {return event_month;}

    public void setEvent_month(String month) {this.event_month = month;}


    public String getEvent_day() {return event_day;}

    public void setEvent_day(String day) {this.event_day = day;}


    public int getEvent_col() {return event_col;}

    public void setEvent_col(int c) {this.event_col = c;}



}
