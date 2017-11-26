package com.seg2105.doooge.choreassistant;


import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by dustin on 11/25/17.
 */

public class Chore implements Serializable {

    private String description;
    private String choreName;
    private Long timeInMillis;
    private Calendar cal;
    private Boolean complete;

    /*
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    */

    //public Chore(String choreName, String description, int day, int month, int year, int hour, int minute ){

    //public Chore(String choreName, String description, Calendar cal){
    public Chore(String choreName, String description, long timeInMillis){
        this.choreName = choreName;
        this.description = description;
        //this.cal = cal;
        this.timeInMillis = timeInMillis;
        complete = false;


        /*
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        */
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getChoreName(){
        return choreName;
    }

    public void setChoreName(String choreName){
        this.choreName = choreName;
    }

    public Calendar getCalendar(){
        return cal;
    }

    public void setCalendar(Calendar cal){
        this.cal = cal;
    }

    public void setComplete(boolean value){
        this.complete = value;
    }

    public Boolean getComplete(){
        return complete;
    }



    /*

    public int getDay(){
        return day;
    }

    public void setDay(int day){
        this.day = day;
    }

    public int getMonth(){
        return month;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getYear(){
        return year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getHour(){
        return hour;
    }

    public void setHour(int hour){
        this.hour= hour;
    }

    public int getMinute(){
        return minute;
    }

    public void setMinute(int minute){
        this.minute = minute;
    }
    */

}
