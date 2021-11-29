package com.josemillanes.covidassist;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {

    private int userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userImg;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Usuario(int userId, String userName, String userEmail, String userImg) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userImg = userImg;
    }
    public Usuario(String userName, String userEmail, String userImg) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userImg = userImg;
    }
    public Usuario(int id, String userEmail, String userPassword) {
        this.userId = id;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }




}
