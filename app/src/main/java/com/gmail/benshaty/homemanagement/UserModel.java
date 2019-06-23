package com.gmail.benshaty.homemanagement;

import java.io.Serializable;
import java.util.Calendar;

public class UserModel implements Serializable {
    private String userEmail;
    private String userName;
    private int userLevel;
    private String uid;
    private int timeToPay;
    private int totalTime;

    public UserModel() {}

    public UserModel(String userEmail, String userName, int userLevel, String uid) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userLevel = userLevel;
        this.uid = uid;
        Calendar c = Calendar.getInstance();
        this.totalTime = 11 - c.get(Calendar.MONTH);
        this.timeToPay = totalTime;

    }

    public UserModel(String userEmail, String userName, int userLevel, String uid, int totalTime, int timeToPay) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userLevel = userLevel;
        this.uid = uid;
        Calendar c = Calendar.getInstance();
        this.totalTime = totalTime;
        this.timeToPay = timeToPay;

    }

    public String getUserEmail() {return userEmail;}
    public String getUserName() {return userName;}
    public int getUserLevel() {return userLevel;}
    public int getTotalTime() {return totalTime;}
    public int getTimeToPay() {return timeToPay;}
    public String getUid() {return uid;}
    public void setTimeToPay(int month) throws IndexOutOfBoundsException {
        if (month > timeToPay) {
            throw new IndexOutOfBoundsException("Cant pay more then you need");
        }
        this.timeToPay = this.timeToPay - month;
    }
    public void addTimeToPay(int month){
        this.timeToPay = this.timeToPay + month;
    }
    public void setTotalTime(int month, int pass){
        if (pass==1234) {
            this.totalTime += month;
        }
    }
}