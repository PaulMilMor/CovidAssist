package com.josemillanes.covidassist;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    private String userId;
    private String userName;
    private String userEmail;
    private String userImg;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public Usuario(String userId, String userName, String userEmail, String userImg) {
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


    //Este método se usa cuando se quiera añadir un usuario a Firestore
    public Map<String, Object> toMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userName", userName);
        userMap.put("userEmail", userEmail);
        userMap.put("userImg", userImg);
        return userMap;
    }



}
