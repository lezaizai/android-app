package com.didlink.xingxing.models;


import com.didlink.xingxing.config.Constants;

/**
 * Created by xingxing on 2016/7/10.
 */
public class Thread {
    private long thid;
    private long tid;
    private Contact author;

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

    public Contact getAuthor() {
        return author;
    }

    public void setAuthor(Contact author) {
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

    public ThreadRealmObj toRealmObj() {
        ThreadRealmObj realmObj = new ThreadRealmObj();
        realmObj.setThid(this.getThid());
        realmObj.setTid(this.getTid());
        realmObj.setStatus(this.getStatus());
        realmObj.setAuthor(this.getAuthor().toRealmObj());
        realmObj.setContent(this.getContent());

        return realmObj;
    }

    public String toString() {
        return  "thid: " + thid + "\n" +
                "tid: " + tid + "\n" +
                "uid: " + author.getUid() + "\n" +
                "status: " + status + "\n" +
                "content: " + content;
    }

}
