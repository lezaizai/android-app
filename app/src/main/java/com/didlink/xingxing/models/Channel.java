package com.didlink.xingxing.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by xingxing on 2016/7/10.
 */
public class Channel {
    private String chid;

    private int type;
    private int status;
    private String name;
    private String description;
    private int contacts_num;
    private Contact owner;
    private Channel parent;
    private List<Channel> children;
    private List<Contact> contacts;
    private List<Topic> topics;

    public Channel(){}

    public Channel(String chid, int type, String name) {
        this.chid = chid;
        this.type = type;
        this.name = name;
        children = new ArrayList<>();
        contacts  = new ArrayList<>();
        topics = new ArrayList<>();
    }

    public void setChid(String chid) {
        this.chid = chid;
    }
    public String getChid() {
        return this.chid;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return this.type;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus() {
        return this.status;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }

    public Channel getParent() {
        return this.parent;
    }
    public void setParent(Channel realmObj) {
        this.parent = realmObj;
    }
    public void setOwner(Contact owner) {
        this.owner = owner;
    }
    public Contact getOwner() {
        return this.owner;
    }
    public void setContacts_num(int num) {
        this.contacts_num = num;
    }
    public int getContacts_num() {
        return this.contacts_num;
    }

    public Channel addChild(Channel childChannel) {
        childChannel.parent = this;
        children.add(childChannel);
        return this;
    }

    public Channel addChild(int index, Channel childChannel) {
        childChannel.parent = this;
        children.add(index, childChannel);
        return this;
    }

    public Channel addChildren(Channel... channels) {
        for (Channel n : channels) {
            addChild(n);
        }
        return this;
    }

    public Channel addChildren(Collection<Channel> channels) {
        for (Channel n : channels) {
            addChild(n);
        }
        return this;
    }

    public int deleteChild(Channel child) {
        for (int i = 0; i < children.size(); i++) {
            if (child.chid == children.get(i).chid) {
                children.remove(i);
                return i;
            }
        }
        return -1;
    }

    public Channel freshChannel(Channel child) {
        for (int i = 0; i < children.size(); i++) {
            if (child.chid == children.get(i).chid) {
                children.remove(i);
                addChild(0,child);
                break;
            }
        }
        return this;
    }

    public List<Channel> getChildren() {
        return children;
    }

    public Channel addContact(Contact contact) {
        contacts.add(contact);
        return this;
    }

    public Channel addContact(int index, Contact contact) {
        contacts.add(index, contact);
        return this;
    }

    public Channel addContacts(Contact... contacts) {
        for (Contact n : contacts) {
            addContact(n);
        }
        return this;
    }

    public Channel addContacts(Collection<Contact> contacts) {
        for (Contact n : contacts) {
            addContact(n);
        }
        return this;
    }

    public int deleteContact(Contact contact) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contact.getUid() == contacts.get(i).getUid()) {
                contacts.remove(i);
                return i;
            }
        }
        return -1;
    }

    public int freshChannel(Contact contact) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contact.getUid() == contacts.get(i).getUid()) {
                contacts.remove(i);
                addContact(0, contact);
                if (parent != null) {
                    parent.freshChannel(this);
                    return 1;
                }
                break;
            }
        }
        return 0;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void addTopic(Topic topic) {
        this.topics.add(topic);
    }

    public ChannelRealmObj toChannelRealmObj() {
        ChannelRealmObj channelObj = new ChannelRealmObj();
        channelObj.setChid(this.getChid());
        channelObj.setType(this.getType());
        channelObj.setStatus(this.getStatus());
        channelObj.setName(this.getName());
        channelObj.setDescription(this.getDescription());
        channelObj.setParent(this.getParent()==null? null : this.getParent().toChannelRealmObj());
        channelObj.setOwner(this.getOwner()==null? null : this.getOwner().toRealmObj());

        int childrens = this.getChildren()==null? 0 : this.getChildren().size();
        for (int i = 0; i < childrens; i++) {
            channelObj.addChild(this.getChildren().get(i).toChannelRealmObj());
        }
        for (int i = 0; i < this.contacts_num; i++) {
            channelObj.addContact(this.getContacts().get(i).toRealmObj());
        }
        channelObj.setContacts_num(this.getContacts_num());

        return channelObj;
    }

    public String toString() {
        return "chid: " + chid + "\n" +
                "type: " + type + "\n" +
                "status: " + status + "\n" +
                "name: " + name + "\n" +
                "owner id: " + (getOwner() == null? "" : getOwner().getUid()) + "\n" +
                "owner nickname: " + (getOwner() == null? "" : getOwner().getNickname());
    }
}
