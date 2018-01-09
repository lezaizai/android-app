package com.didlink.xingxing.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xingxing on 2016/7/10.
 */
public class Channel extends RealmObject {
    @PrimaryKey
    private String chid;

    private int type;
    private String name;
    private Channel parent;
    private Contact owner;
    private RealmList<Channel> children;
    private RealmList<Contact> contacts;
    private int contacts_num;

    public Channel(String chid, int type, String name) {
        this.chid = chid;
        this.type = type;
        this.name = name;
        children = new RealmList<>();
        contacts  = new RealmList<>();
    }

    public String getChid() {
        return this.chid;
    }
    public int getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
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
        return Collections.unmodifiableList(children);
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
        return Collections.unmodifiableList(contacts);
    }

}
