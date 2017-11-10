package com.realm.sqlite.adapter;

import android.content.Context;

import com.realm.sqlite.model.Kitap;
import io.realm.RealmResults;

public class RealmBooksAdapter extends RealmModelAdapter<Kitap> {

    public RealmBooksAdapter(Context context, RealmResults<Kitap> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}