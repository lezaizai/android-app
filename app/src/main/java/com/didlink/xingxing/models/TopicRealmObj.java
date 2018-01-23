package com.didlink.xingxing.models;


import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by xingxing on 2016/7/10.
 */
public class TopicRealmObj extends RealmObject{
    private long tid;
    private long chid;
    private ContactRealmObj author;

    private byte status;
    private String subject;
    private long lastupdate;
    private long createtime;
    private RealmList<ThreadRealmObj> threads;

    public TopicRealmObj() {
        threads = new RealmList<>();
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void addThread(ThreadRealmObj thread) {
        this.threads.add(thread);
    }
    public RealmList<ThreadRealmObj> getThreads() {
        return threads;
    }

    public Topic toTopic() {
        Topic topic = new Topic();
        topic.setTid(this.getTid());
        topic.setChid(this.getChid());
        topic.setAuthor(this.getAuthor().toContact());
        topic.setStatus(this.getStatus());
        topic.setSubject(this.getSubject());

        return topic;

    }

    public String toString() {
        return  "tid: " + tid + "\n" +
                "chid: " + chid + "\n" +
                "uid: " + author.getUid() + "\n" +
                "subject: " + subject;
    }

}
