package com.gmail.benshaty.homemanagement;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String userEmail;
    private String userName;
    private int userLevel;
    private String uid;

    public UserModel() {}

    public UserModel(String userEmail, String userName, int userLevel, String uid) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userLevel = userLevel;
        this.uid = uid;
    }

    public String getUserEmail() {return userEmail;}
    public String getUserName() {return userName;}
    public int getUserLevel() {return userLevel;}
    public String getUid() {return uid;}
}