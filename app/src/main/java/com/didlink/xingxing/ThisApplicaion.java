package com.didlink.xingxing;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by dad on 08/01/2018.
 */

public class ThisApplicaion extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("xing.realm").build();
        Realm.setDefaultConfiguration(config);
    }
}
