package com.didlink.xingxing.models;


/**
 * Created by xingxing on 2016/7/10.
 */
public class Contact {
    private long uid;

    private byte status;
    private boolean online;
    private String username;
    private String nickname;
    private String avatar;

    public Contact(){}

    public Contact(long uid, String username, String nickname) {
        this.uid = uid;
        this.username = username;
        this.nickname = nickname;
    }

    public Contact(long uid, String username, String nickname, String avatar) {
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

    public ContactRealmObj toRealmObj() {
        ContactRealmObj contactObj = new ContactRealmObj(this.getUid(),
                this.getUsername(),
                this.getNickname(),
                this.getAvatar());
        return contactObj;
    }
}
