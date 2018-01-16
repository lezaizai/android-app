package com.didlink.xingxing.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xingxing on 2016/7/10.
 */
public class Contact {
    @PrimaryKey
    private long uid;

    private boolean online;
    private String username;
    private String nickname;
    private String gravatarpicture;

    public Contact(){}

    public Contact(long uid, String username, String nickname) {
        this.uid = uid;
        this.username = username;
        this.nickname = nickname;
    }

    public Contact(long uid, String username, String nickname, String gravatarpicture) {
        this.uid = uid;
        this.username = username;
        this.nickname = nickname;
        this.gravatarpicture = gravatarpicture;
    }

    public long getUid(){
        return this.uid;
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

    public ContactRealmObj toContactRealmObj() {
        ContactRealmObj contactObj = new ContactRealmObj(this.getUid(),
                this.getUsername(),
                this.getNickname(),
                this.getGravatarpicture());
        return contactObj;
    }
}
