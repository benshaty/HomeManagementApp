package com.gmail.benshaty.homemanagement;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String userEmail;
    private String userName;
    private int userLevel;

    public UserModel() {}

    public UserModel(String userEmail, String userName, int userLevel) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userLevel = userLevel;
    }

    public String getUserEmail() {return userEmail;}
    public String getUserName() {return userName;}
    public int getUserLevel() {return userLevel;}
}