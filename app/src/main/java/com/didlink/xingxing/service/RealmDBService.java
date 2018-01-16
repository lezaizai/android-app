package com.didlink.xingxing.service;

import android.util.Log;

import com.didlink.xingxing.models.Channel;
import com.didlink.xingxing.models.ChannelRealmObj;
import com.didlink.xingxing.models.LoginAuth;
import com.didlink.xingxing.models.LoginAuthRealmObj;

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
            realm.copyToRealmOrUpdate(auth.toRealmOjb());
            realm.commitTransaction();

            return true;
        }
    }

    public LoginAuth getLoginAuth() {
        try (Realm realm = Realm.getDefaultInstance()) {
            // No need to close the Realm instance manually
            long cnt = realm.where(LoginAuthRealmObj.class).count();
            Log.i(TAG, "Login Auth count: "  + cnt);
            if (cnt > 0) {
                return realm.where(LoginAuthRealmObj.class).findFirst().toAuth();
            } else {
                return null;
            }
        }

    }

    public List<Channel> getChannels() {
        List<Channel> channels = new ArrayList<>();

        try (Realm realm = Realm.getDefaultInstance()) {
            // No need to close the Realm instance manually
            Log.i(TAG, "Channel count: "  + realm.where(ChannelRealmObj.class).count());
            List<ChannelRealmObj> channelRealmObjs = realm.where(ChannelRealmObj.class).findAll();
            for (int i = 0; i<channelRealmObjs.size(); i++) {
                channels.add(channelRealmObjs.get(i).toChannel());
            }
            return channels;
        }

    }

    public boolean joinChannel(Channel channel) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(channel.toChannelRealmObj());
            realm.commitTransaction();

            return true;
        }
    }

    public boolean updateChannel(Channel channel) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(channel.toChannelRealmObj());
            realm.commitTransaction();

            return true;
        }
    }

    public boolean removeChannel(Channel channel) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            RealmResults<ChannelRealmObj> channelRealmResults =  realm.where(ChannelRealmObj.class).equalTo("chid", channel.getChid()).findAll();
            channelRealmResults.deleteAllFromRealm();
            realm.commitTransaction();

            return true;
        }
    }

    public void clearChannels() {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            RealmResults<ChannelRealmObj> channelRealmResults =  realm.where(ChannelRealmObj.class).findAll();
            channelRealmResults.deleteAllFromRealm();
            realm.commitTransaction();
        }
    }

}
