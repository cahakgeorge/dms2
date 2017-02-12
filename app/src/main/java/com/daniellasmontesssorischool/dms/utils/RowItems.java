package com.daniellasmontesssorischool.dms.utils;

/**
 * Created by Cahak on 25/04/2016.
 */
public class RowItems {

    private String date;
    private String art_title;
    private String stud_name;
    private String stud_class;
    private String article_type;
    private String article_content;


    public String getTitle() {
        return art_title;
    }

    public void setTitle(String title) {
        this.art_title = title;
    }

    public String getArticle_type() {
        return article_type;
    }

    public void setArticle_type(String typ) {
        this.article_type = typ;
    }

    public String getArticle_content() {
        return article_content;
    }

    public void setArticle_content(String cont) {
        this.article_content = cont;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getStud_name() {return stud_name;}

    public void setStud_name(String name) {
        this.stud_name = name;
    }


    public String getStud_class() {return stud_class;}

    public void setStud_class(String stclass) {this.stud_class = stclass;}


}
