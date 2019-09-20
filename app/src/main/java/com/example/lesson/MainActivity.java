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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

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
    static Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CONTACT_READ:
                        startTransaction();
                        break;
                }
            }
        };
        int permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            //readContact();
            ReadContactPermission readContactPermission = new ReadContactPermission(activity, context);
            readContactPermission.pemissionGranted();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            //readContacts(context);
        }


    }

    private void startTransaction() {
        contactListFragment = new ContactListFragment();
        fragmentManager.beginTransaction().addToBackStack(null).add(R.id.frag, contactListFragment).commit();
    }

    public void readContact() {

        Runnable runnable = new Runnable() {
            public void run() {
                ReadContactPermission readContactPermission = new ReadContactPermission(activity, context);
                readContactPermission.pemissionGranted();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void setAdapter(RecyclerView recycler) {

        Log.d("1", "adapter");

        ContactListAdapter adapter = new ContactListAdapter(this,this);
        recycler.setAdapter(adapter);
        recycler.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        contactFragment = new ContactFragment();
        contactFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frag,contactFragment).addToBackStack(null).commit();
        Log.d("CLICK", "CLICK "+position);
    }
}
