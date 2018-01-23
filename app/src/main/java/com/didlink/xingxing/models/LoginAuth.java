package com.didlink.xingxing.models;

/**
 * Created by wuh56 on 3/24/2017.
 */
public class LoginAuth {
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

    public LoginAuthRealmObj toRealmOjb() {
        LoginAuthRealmObj realmObj = new LoginAuthRealmObj();
        realmObj.setUid(this.getUid());
        realmObj.setAvatar(this.getAvatar());
        realmObj.setBaseurl(this.getBaseurl());
        realmObj.setNickname(this.getNickname());
        realmObj.setPassword(this.getPassword());
        realmObj.setStatus(this.getStatus());
        realmObj.setToken(this.getToken());
        realmObj.setUsername(this.getUsername());

        return realmObj;
    }

    public Contact toContact() {
        Contact contact = new Contact(this.getUid(), this.getUsername(), this.getNickname());
        contact.setAvatar(this.getAvatar());
        return contact;
    }

    public String toString() {
        return  "uid: " + uid + "\n" +
                "status: " + status + "\n" +
                "username: " + username + "\n" +
                "password: " + password + "\n" +
                "token: " + token;
    }

}
