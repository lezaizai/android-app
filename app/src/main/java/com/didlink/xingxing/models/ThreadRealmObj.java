package com.didlink.xingxing.models;


import com.didlink.xingxing.config.Constants;

import io.realm.RealmObject;

/**
 * Created by xingxing on 2016/7/10.
 */
public class ThreadRealmObj extends RealmObject {
    private long thid;
    private long tid;
    private ContactRealmObj author;

    private byte status;
    private String content;
    private long lastupdate;
    private long createtime;

    public long getThid() {
        return thid;
    }

    public void setThid(long thid) {
        this.thid = thid;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public ContactRealmObj getAuthor() {
        return author;
    }

    public void setAuthor(ContactRealmObj author) {
        this.author = author;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Thread toThread() {
        Thread thread = new Thread();
        thread.setThid(this.getThid());
        thread.setTid(this.getTid());
        thread.setAuthor(this.getAuthor().toContact());
        thread.setStatus(this.getStatus());
        thread.setContent(this.getContent());

        return thread;
    }

    public String toString() {
        return  "thid: " + thid + "\n" +
                "tid: " + tid + "\n" +
                "uid: " + author.getUid() + "\n" +
                "status: " + status + "\n" +
                "content: " + content;
    }

}
