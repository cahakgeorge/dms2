package com.daniellasmontesssorischool.dms.utils;
/**
 * Created by Cahak on 25/04/2016.
 */
public class RowItemsAssignment {
    private String ass_title;
    private String ass_content;
    private String ass_extrainfo;
    private String ass_qtopic;

    private String ass_teacher;
    private String ass_subdate;
    private String ass_initials;
    private String ass_class;
    private int lin_assignleft;


    public String getTitle() {
        return ass_title;
    }

    public void setTitle(String title) {
        this.ass_title = title;
    }

    public String getAss_class() {
        return ass_class;
    }

    public void setAss_class(String cla) {
        this.ass_class = cla;
    }


    public String getDate() {
        return ass_subdate;
    }

    public void setDate(String date) {
        this.ass_subdate = date;
    }


    public String getAss_content() {return ass_content;}

    public void setAss_content(String content) {
        this.ass_content = content;
    }


    public String getAss_extrainfo(){return ass_extrainfo;}

    public void setAss_extrainfo(String inf){ass_extrainfo = inf;}


    public String getAss_qtopic(){return ass_qtopic;}

    public void setAss_qtopic (String qtop){ass_qtopic = qtop;}


    public String getAss_teacher() {return ass_teacher;}

    public void setAss_teacher(String teacher) {this.ass_teacher = teacher;}


    public String getAss_initials() {return ass_initials;}

    public void setAss_initials(String initials) {this.ass_initials = initials;}


    public int getLin_assignleft() {return lin_assignleft;}

    public void setLin_assignleft(int c) {this.lin_assignleft = c;}

}
