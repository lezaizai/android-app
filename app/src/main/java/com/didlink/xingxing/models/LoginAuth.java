package com.didlink.xingxing.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wuh56 on 3/24/2017.
 */
public class LoginAuth extends RealmObject {
    @PrimaryKey
    private String uid;

    private boolean status;
    private String baseurl;
    private String username;
    private String nickname;
    private String password;
    private String avatar;
    private String token;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toString() {
        return "username: " + username + "\n" +
                "password: " + password + "\n" +
                "token: " + token;
    }

}
