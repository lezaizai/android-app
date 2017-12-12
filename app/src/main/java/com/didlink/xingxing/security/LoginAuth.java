package com.didlink.xingxing.security;

/**
 * Created by wuh56 on 3/24/2017.
 */
public class LoginAuth {
    private boolean status;
    private String baseurl;
    private String username;
    private String password;
    private String token;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
