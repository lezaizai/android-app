package com.didlink.xingxing.service;

import android.util.Log;

import com.didlink.xingxing.AppSingleton;
import com.didlink.xingxing.models.Channel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by xingxing on 2016/5/15.
 */
public class RealmDBChannelService {

    public static final String TAG = RealmDBChannelService.class.getName();
    private AppSingleton app;


    public static List<Channel> getChannels() {
        List<Channel> channels = new ArrayList<>();

        try (Realm realm = Realm.getDefaultInstance()) {
            // No need to close the Realm instance manually
            Log.i(TAG, "Channel count: "  + realm.where(Channel.class).count());
            channels = realm.where(Channel.class).findAll();
            return channels;
        }

    }

    public static boolean joinChannel(Channel channel) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            realm.copyToRealm(channel);
            realm.commitTransaction();

            return true;
        }
    }

    public static boolean updateChannel(Channel channel) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(channel);
            realm.commitTransaction();

            return true;
        }
    }

    public static boolean removeChannel(Channel channel) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            RealmResults<Channel> channelRealmResults =  realm.where(Channel.class).equalTo("chid", channel.getChid()).findAll();
            channelRealmResults.deleteAllFromRealm();
            realm.commitTransaction();

            return true;
        }
    }

    public static void clearChannels() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            RealmResults<Channel> channelRealmResults =  realm.where(Channel.class).findAll();
            channelRealmResults.deleteAllFromRealm();
            realm.commitTransaction();
        }
    }

}
