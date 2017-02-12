package com.daniellasmontesssorischool.dms.utils;

/**
 * Created by Cahak on 25/04/2016.
 */
public class RowItemsNews {

    private String news_date;
    private String news_title;
    private String news_content;
    private String news_type;
    private int newstitle_col;
    private int newsdetail_col;
    private int newsbottom_col;

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String title) {
        this.news_title = title;
    }


    public String getNews_content() {return news_content;}

    public void setNews_content(String content) {
        this.news_content = content;
    }

    public String getNews_type() {return news_type;}

    public void setNews_type(String type) {
        this.news_type = type;
    }


    public String getDate() {
        return news_date;
    }

    public void setDate(String date) {
        this.news_date = date;
    }

    public int getNewstitle_col() {return newstitle_col;}
    public int getNewsdetail_col() {return newsdetail_col;}
    public int getNewsbottom_col() {return newsbottom_col;}

    public void setCol(int c1, int c2, int c3) {this.newstitle_col = c1; this.newsdetail_col = c2; this.newsbottom_col = c3;}

}
