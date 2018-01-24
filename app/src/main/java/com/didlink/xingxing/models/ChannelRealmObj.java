package com.didlink.xingxing.models;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xingxing on 2016/7/10.
 */
public class ChannelRealmObj extends RealmObject {
    @PrimaryKey
    private String chid;

    private int type;
    private int status;
    private String name;
    private String description;
    private ChannelRealmObj parent;
    private ContactRealmObj owner;
    private RealmList<ChannelRealmObj> children;
    private RealmList<ContactRealmObj> contacts;
    private int contacts_num;

    public ChannelRealmObj(){}

    public ChannelRealmObj(String chid, int type, String name) {
        this.chid = chid;
        this.type = type;
        this.name = name;
        children = new RealmList<>();
        contacts  = new RealmList<>();
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
    public void setStatus(int status) {
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

    public ChannelRealmObj getParent() {
        return this.parent;
    }
    public void setParent(ChannelRealmObj realmObj) {
        this.parent = realmObj;
    }
    public void setOwner(ContactRealmObj owner) {
        this.owner = owner;
    }
    public ContactRealmObj getOwner() {
        return this.owner;
    }
    public void setContacts_num(int num) {
        this.contacts_num = num;
    }
    public int getContacts_num() {
        return this.contacts_num;
    }

    public ChannelRealmObj addChild(ChannelRealmObj childChannel) {
        childChannel.parent = this;
        children.add(childChannel);
        return this;
    }

    public ChannelRealmObj addChild(int index, ChannelRealmObj childChannel) {
        childChannel.parent = this;
        children.add(index, childChannel);
        return this;
    }

    public ChannelRealmObj addChildren(ChannelRealmObj... channels) {
        for (ChannelRealmObj n : channels) {
            addChild(n);
        }
        return this;
    }

    public ChannelRealmObj addChildren(Collection<ChannelRealmObj> channels) {
        for (ChannelRealmObj n : channels) {
            addChild(n);
        }
        return this;
    }

    public int deleteChild(ChannelRealmObj child) {
        for (int i = 0; i < children.size(); i++) {
            if (child.chid == children.get(i).chid) {
                children.remove(i);
                return i;
            }
        }
        return -1;
    }

    public ChannelRealmObj freshChannel(ChannelRealmObj child) {
        for (int i = 0; i < children.size(); i++) {
            if (child.chid == children.get(i).chid) {
                children.remove(i);
                addChild(0,child);
                break;
            }
        }
        return this;
    }

    public List<ChannelRealmObj> getChildren() {
        return children;
    }

    public ChannelRealmObj addContact(ContactRealmObj contact) {
        contacts.add(contact);
        return this;
    }

    public ChannelRealmObj addContact(int index, ContactRealmObj contact) {
        contacts.add(index, contact);
        return this;
    }

    public ChannelRealmObj addContacts(ContactRealmObj... contacts) {
        for (ContactRealmObj n : contacts) {
            addContact(n);
        }
        return this;
    }

    public ChannelRealmObj addContacts(Collection<ContactRealmObj> contacts) {
        for (ContactRealmObj n : contacts) {
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

    public int freshChannel(ContactRealmObj contact) {
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

    public List<ContactRealmObj> getContacts() {
        return Collections.unmodifiableList(contacts);
    }

    public Channel toChannel() {
        Channel channel = new Channel();
        channel.setChid(this.getChid());
        channel.setType(this.getType());
        channel.setStatus(this.getStatus());
        channel.setParent(this.getParent()==null ? null : this.getParent().toChannel());
        channel.setOwner(this.getOwner()==null ? null : this.getOwner().toContact());

        int childrens = this.getChildren()==null ? 0 : this.getChildren().size();
        for (int i = 0; i < childrens; i++) {
            channel.addChild(this.getChildren().get(i).toChannel());
        }
        for (int i = 0; i < this.contacts_num; i++) {
            channel.addContact(this.getContacts().get(i).toContact());
        }
        channel.setContacts_num(this.getContacts_num());

        return channel;
    }
}
