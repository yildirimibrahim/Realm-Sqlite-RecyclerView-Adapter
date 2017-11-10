package com.realm.sqlite.applicaiton;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ibrahim yildirim on 07/11/2017.
 */

public class AppApplication extends Application {

    public static String dbName  = "KitapVerileri";
    public static int dbVersiyon = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(dbName)
                .schemaVersion(dbVersiyon)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }
}
