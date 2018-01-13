package com.didlink.xingxing.service;

import android.util.Log;

import com.didlink.xingxing.models.Channel;
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

    private LoginAuth converToAuth(LoginAuthRealmObj realmObj) {
        LoginAuth auth = new LoginAuth();
        auth.setUid(realmObj.getUid());
        auth.setAvatar(realmObj.getAvatar());
        auth.setBaseurl(realmObj.getBaseurl());
        auth.setNickname(realmObj.getNickname());
        auth.setPassword(realmObj.getPassword());
        auth.setStatus(realmObj.getStatus());
        auth.setToken(realmObj.getToken());
        auth.setUsername(realmObj.getUsername());

        return auth;
    }

    private LoginAuthRealmObj converFromAuth(LoginAuth authObj) {
        LoginAuthRealmObj realmObj = new LoginAuthRealmObj();
        realmObj.setUid(authObj.getUid());
        realmObj.setAvatar(authObj.getAvatar());
        realmObj.setBaseurl(authObj.getBaseurl());
        realmObj.setNickname(authObj.getNickname());
        realmObj.setPassword(authObj.getPassword());
        realmObj.setStatus(authObj.getStatus());
        realmObj.setToken(authObj.getToken());
        realmObj.setUsername(authObj.getUsername());

        return realmObj;
    }

}
