package com.realm.sqlite;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.realm.sqlite.adapter.KitapAdapter;
import com.realm.sqlite.adapter.RealmBooksAdapter;
import com.realm.sqlite.model.Kitap;
import com.realm.sqlite.preferences.AppSharedPreferences;
import com.realm.sqlite.realm.RealmController;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recycler;
    private Realm realm;
    private KitapAdapter adapter;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(FloatingListener);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        this.realm = RealmController.with(this).getRealm();

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupRecycler();

        if (!AppSharedPreferences.with(this).getPreferenceLoad()) {
            setRealmData();
        }

        RealmController.with(this).refresh();

        setRealmAdapter(RealmController.with(this).getBooks());

        Toast.makeText(this,"Öğeyi kaldırmak için uzun basın.",Toast.LENGTH_LONG).show();
    }

    public void setRealmAdapter(RealmResults<Kitap> books) {

        RealmBooksAdapter realmAdapter = new RealmBooksAdapter(this.getApplicationContext(), books, true);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        adapter = new KitapAdapter(this);
        recycler.setAdapter(adapter);
    }
    private void setRealmData() {

        ArrayList<Kitap> kitaplar = new ArrayList<>();

        Kitap kitap = new Kitap();
        kitap.setId(1 + System.currentTimeMillis());
        kitap.setAuthor("Reto Meier");
        kitap.setTitle("Android 4 AppApplication Development");
        kitap.setImageUrl("http://api.androidhive.info/images/realm/1.png");
        kitaplar.add(kitap);

        kitap = new Kitap();
        kitap.setId(2 + System.currentTimeMillis());
        kitap.setAuthor("Itzik Ben-Gan");
        kitap.setTitle("Microsoft SQL Server 2012 T-SQL Fundamentals");
        kitap.setImageUrl("http://api.androidhive.info/images/realm/2.png");
        kitaplar.add(kitap);

        kitap = new Kitap();
        kitap.setId(3 + System.currentTimeMillis());
        kitap.setAuthor("Magnus Lie Hetland");
        kitap.setTitle("Beginning Python: From Novice To Professional Paperback");
        kitap.setImageUrl("http://api.androidhive.info/images/realm/3.png");
        kitaplar.add(kitap);

        kitap = new Kitap();
        kitap.setId(4 + System.currentTimeMillis());
        kitap.setAuthor("Chad Fowler");
        kitap.setTitle("The Passionate Programmer: Creating a Remarkable Career in Software Development");
        kitap.setImageUrl("http://api.androidhive.info/images/realm/4.png");
        kitaplar.add(kitap);

        kitap = new Kitap();
        kitap.setId(5 + System.currentTimeMillis());
        kitap.setAuthor("Yashavant Kanetkar");
        kitap.setTitle("Written Test Questions In C Programming");
        kitap.setImageUrl("http://api.androidhive.info/images/realm/5.png");
        kitaplar.add(kitap);


        for (Kitap b : kitaplar) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();
        }

        AppSharedPreferences.with(this).setPreferenceLoad(true);
    }

    View.OnClickListener FloatingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            inflater = MainActivity.this.getLayoutInflater();
            View content = inflater.inflate(R.layout.edit_item, null);
            final EditText editTitle = (EditText) content.findViewById(R.id.title);
            final EditText editAuthor = (EditText) content.findViewById(R.id.author);
            final EditText editThumbnail = (EditText) content.findViewById(R.id.thumbnail);


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(content)
                    .setTitle("Add book")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Kitap kitap = new Kitap();
                            //book.setId(RealmController.getInstance().getBooks().size() + 1);
                            kitap.setId(RealmController.getInstance().getBooks().size() + System.currentTimeMillis());
                            kitap.setTitle(editTitle.getText().toString());
                            kitap.setAuthor(editAuthor.getText().toString());
                            kitap.setImageUrl(editThumbnail.getText().toString());

                            if (editTitle.getText() == null || editTitle.getText().toString().equals("") || editTitle.getText().toString().equals(" ")) {
                                Toast.makeText(MainActivity.this, "Giriş kaydedilmedi, başlık eksik", Toast.LENGTH_SHORT).show();
                            } else {
                                // Persist your data easily
                                realm.beginTransaction();
                                realm.copyToRealm(kitap);
                                realm.commitTransaction();

                                adapter.notifyDataSetChanged();

                                // scroll the recycler view to bottom
                                recycler.scrollToPosition(RealmController.getInstance().getBooks().size() - 1);
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };
}
