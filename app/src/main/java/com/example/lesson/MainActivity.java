package com.example.lesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements ContactListAdapter.OnCliclListner {

    final static int CONTACT_READ = 0;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    ContactListFragment contactListFragment;
    ContactFragment contactFragment;
    public FragmentManager fragmentManager = getSupportFragmentManager();
    Context context = this;
    Activity activity = this;
    MainActivity mainActivity = this;
    Handler handler;
    int savePosition;
    List<ContactDB> contactDBS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CONTACT_READ:
                        Thread tr = new Thread(readDB);
                        tr.start();
                        startTransaction();
                        break;
                }
            }
        };

        int permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            Thread thread = new Thread(runnable);
            thread.start();

        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        }

    }


    private void startTransaction() {

        contactListFragment = new ContactListFragment();
        fragmentManager.beginTransaction().add(R.id.frag, contactListFragment).commit();
    }

    public void setAdapter(RecyclerView recycler) {

        Log.d("1", "adapter");
        String id = contactDBS.get(0).getIds();
        ContactListAdapter adapter = new ContactListAdapter(this, this, contactDBS);
        recycler.setAdapter(adapter);
        recycler.getAdapter().notifyDataSetChanged();
    }

    Runnable readDB = new Runnable() {
        @Override
        public void run() {
            contactDBS = ContactDB.listAll(ContactDB.class);
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ReadContactPermission readContactPermission = new ReadContactPermission(activity, context, mainActivity);
            readContactPermission.pemissionGranted();
        }
    };

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        savePosition = position;
        bundle.putInt("position", position);
        savePosition = position;
        contactFragment = new ContactFragment();
        contactFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frag, contactFragment).addToBackStack(null).commit();
        Log.d("CLICK", "CLICK " + position);
    }

}
