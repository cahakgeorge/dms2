package com.daniellasmontesssorischool.dms.utils;

/**
 * Created by Cahak on 25/04/2016.
 */
public class RowItemsCalendar {

    private String cal_day;
    private String cal_month;
    private String calimg_title;

    private String cal_title;
    private String cal_detail;
    private String cal_interval;
    private int backpic;

    //private String cal_findoutmore;


    public String getCal_day() {
        return cal_day;
    }
    public void setCal_day(String day) {
        this.cal_day = day;
    }

    public String getCal_month() {
        return cal_month;
    }
    public void setCal_month(String month) {
        this.cal_month = month;
    }

    public String getCalimg_title() {
        return calimg_title;
    }
    public void setCalimg_title(String title) {
        this.calimg_title = title;
    }

    public String getCal_title() {
        return cal_title;
    }
    public void setCal_title(String title) {
        this.cal_title = title;
    }

    public String getCal_detail() {
        return cal_detail;
    }
    public void setCal_detail(String detail) {
        this.cal_detail = detail;
    }

    public String getCal_interval() {
        return cal_interval;
    }
    public void setCal_interval(String interval) {
        this.cal_interval = interval;
    }

    public int getBackpic() {return backpic;}

    public void setBackpic(int pic) {this.backpic = pic;}

}
