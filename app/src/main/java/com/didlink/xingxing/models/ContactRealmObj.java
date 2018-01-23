package com.didlink.xingxing.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xingxing on 2016/7/10.
 */
public class ContactRealmObj extends RealmObject {
    @PrimaryKey
    private long uid;

    private byte status;
    private boolean online;
    private String username;
    private String nickname;
    private String avatar;

    public ContactRealmObj(){}

    public ContactRealmObj(long uid, String username, String nickname) {
        this.uid = uid;
        this.username = username;
        this.nickname = nickname;
    }

    public ContactRealmObj(long uid, String username, String nickname, String avatar) {
        this.uid = uid;
        this.username = username;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public long getUid(){
        return this.uid;
    }
    public byte getStatus() {
        return status;
    }
    public void setStatus(byte status) {
        this.status = status;
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
    public void setAvatar(String avatar){
        this.avatar = avatar;
    }
    public String getAvatar(){
        return this.avatar;
    }

    public Contact toContact() {
        Contact contact = new Contact(this.getUid(),
                this.getUsername(),
                this.getNickname(),
                this.getAvatar());
        return contact;
    }

}
