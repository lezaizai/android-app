package com.didlink.xingxing;

import android.app.Application;
import android.util.Log;

import com.didlink.xingxing.models.LoginAuth;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by dad on 08/01/2018.
 */

public class ThisApplicaion extends Application {
    private static final String TAG = ThisApplicaion.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("xing.realm").deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);


        LoginAuth auth = AppSingleton.getInstance().getmRealmDBService().getLoginAuth();
        if (auth == null) {
            auth = new LoginAuth();
            auth.setStatus((byte)1);
        }
        AppSingleton.getInstance().setLoginAuth(auth);

        Log.i(TAG, " started. " + auth.toString());
    }
}
