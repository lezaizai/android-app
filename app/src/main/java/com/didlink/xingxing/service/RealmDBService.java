package com.didlink.xingxing.service;

import android.util.Log;

import com.didlink.xingxing.models.Channel;
import com.didlink.xingxing.models.LoginAuth;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by xingxing on 2016/5/15.
 */
public class RealmDBService {

    public static final String TAG = RealmDBService.class.getName();

    public boolean saveAuth(LoginAuth auth) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(auth);
            realm.commitTransaction();

            return true;
        }
    }

    public LoginAuth getLoginAuth() {
        try (Realm realm = Realm.getDefaultInstance()) {
            // No need to close the Realm instance manually
            Log.i(TAG, "Channel count: "  + realm.where(Channel.class).count());
            return realm.where(LoginAuth.class).findFirst();
        }

    }

    public List<Channel> getChannels() {
        List<Channel> channels = new ArrayList<>();

        try (Realm realm = Realm.getDefaultInstance()) {
            // No need to close the Realm instance manually
            Log.i(TAG, "Channel count: "  + realm.where(Channel.class).count());
            channels = realm.where(Channel.class).findAll();
            return channels;
        }

    }

    public boolean joinChannel(Channel channel) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(channel);
            realm.commitTransaction();

            return true;
        }
    }

    public boolean updateChannel(Channel channel) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(channel);
            realm.commitTransaction();

            return true;
        }
    }

    public boolean removeChannel(Channel channel) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            RealmResults<Channel> channelRealmResults =  realm.where(Channel.class).equalTo("chid", channel.getChid()).findAll();
            channelRealmResults.deleteAllFromRealm();
            realm.commitTransaction();

            return true;
        }
    }

    public void clearChannels() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            RealmResults<Channel> channelRealmResults =  realm.where(Channel.class).findAll();
            channelRealmResults.deleteAllFromRealm();
            realm.commitTransaction();
        }
    }

}
