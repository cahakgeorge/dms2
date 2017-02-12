package com.daniellasmontesssorischool.dms.utils;

/**
 * Created by Cahak on 25/04/2016.
 */
public class RowItemsMTeachers {

    private String dateofbirth;
    private String teacher_access;
    private String teacher_name;
    private String username;
    private String gender;

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dob) {
        this.dateofbirth = dob;
    }

    public String getTeacher_access() {return teacher_access;}

    public void setTeacher_access(String acce) {
        this.teacher_access = acce;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String nm) {
        this.teacher_name = nm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String usr) {
        this.username = usr;
    }


    public String getGender() {return gender;}

    public void setGender(String gnd) {
        this.gender = gnd;
    }

}
