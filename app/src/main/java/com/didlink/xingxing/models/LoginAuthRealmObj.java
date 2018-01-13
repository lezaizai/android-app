package com.didlink.xingxing.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wuh56 on 3/24/2017.
 */
public class LoginAuthRealmObj extends RealmObject {
    @PrimaryKey
    private long uid;

    private byte status;
    private String baseurl;
    private String username;
    private String nickname;
    private String password;
    private String avatar;
    private String token;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
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

    public LoginAuth toAuth() {
        LoginAuth auth = new LoginAuth();
        auth.setUid(this.getUid());
        auth.setAvatar(this.getAvatar());
        auth.setBaseurl(this.getBaseurl());
        auth.setNickname(this.getNickname());
        auth.setPassword(this.getPassword());
        auth.setStatus(this.getStatus());
        auth.setToken(this.getToken());
        auth.setUsername(this.getUsername());

        return auth;
    }

    public String toString() {
        return  "uid: " + uid + "\n" +
                "status: " + status + "\n" +
                "username: " + username + "\n" +
                "password: " + password + "\n" +
                "token: " + token;
    }

}
