package com.didlink.xingxing.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xingxing on 2016/7/10.
 */
public class Contact extends RealmObject {
    @PrimaryKey
    private String uid;

    private boolean online;
    private String username;
    private String nickname;
    private String gravatarpicture;
    private String socketid;
    private String streamid;

    public Contact(String uid, String username, String nickname) {
        this.uid = uid;
        this.username = username;
        this.nickname = nickname;
    }

    public Contact(String uid, String username, String nickname, String gravatarpicture) {
        this.uid = uid;
        this.username = username;
        this.nickname = nickname;
        this.gravatarpicture = gravatarpicture;
    }
    public Contact(String uid, String username, String nickname, String gravatarpicture, String streamid) {
        this.uid = uid;
        this.username = username;
        this.nickname = nickname;
        this.gravatarpicture = gravatarpicture;
        this.streamid = streamid;
    }

    public String getUid(){
        return this.uid;
    }
    public void setSocketid(String id){
        this.socketid = id;
    }
    public String getSocketid(){
        return this.socketid;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }
    public boolean isOnline(){
        return this.online;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public String getNickname(){
        return this.nickname;
    }
    public void setGravatarpicture(String gravatarpicture){
        this.gravatarpicture = gravatarpicture;
    }
    public String getGravatarpicture(){
        return this.gravatarpicture;
    }
    public String getStreamid(){
        return this.streamid;
    }

}
