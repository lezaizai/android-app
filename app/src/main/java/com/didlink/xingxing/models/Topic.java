package com.didlink.xingxing.models;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingxing on 2016/7/10.
 */
public class Topic {
    private long tid;
    private long chid;
    private Contact author;

    private byte status;
    private String subject;
    private long lastupdate;
    private long createtime;
    private List<Thread> threads;

    public Topic() {
        threads = new ArrayList<>();
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public long getChid() {
        return chid;
    }

    public void setChid(long chid) {
        this.chid = chid;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void addThread(Thread thread) {
        this.threads.add(thread);
    }
    public List<Thread> getThreads() {
        return threads;
    }

    public TopicRealmObj toRealmObj() {
        TopicRealmObj realmObj = new TopicRealmObj();
        realmObj.setTid(this.getTid());
        realmObj.setChid(this.getChid());
        realmObj.setAuthor(this.getAuthor().toRealmObj());
        realmObj.setStatus(this.getStatus());
        realmObj.setSubject(this.getSubject());

        for (Thread t : threads) {
            realmObj.addThread(t.toRealmObj());
        }
        return realmObj;
    }

    public String toString() {
        return  "tid: " + tid + "\n" +
                "chid: " + chid + "\n" +
                "uid: " + author.getUid() + "\n" +
                "subject: " + subject;
    }

}
