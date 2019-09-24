package com.example.lesson;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends FragmentActivity implements ContactListAdapter.OnCliclListner {

    final static int CONTACT_READ = 0;
    final static int DB_READ = 1;
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
    String TAG = "MainActivity";
    Thread thread;
    Thread tr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CONTACT_READ:
                        tr = new Thread(new readContactDb(mainActivity));
                        tr.start();
                        break;
                    case DB_READ:
                        startTransaction();
                        break;
                }
            }
        };
        pemissionGranted();

        int permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {


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
        //Long id = contactDBS.get(0).getId();
        ContactListAdapter adapter = new ContactListAdapter(this, this, contactDBS);
        recycler.setAdapter(adapter);
        recycler.getAdapter().notifyDataSetChanged();
    }

    Runnable readDB = new Runnable() {
        @Override
        public void run() {
            if(!Thread.interrupted()) {
                contactDBS = ContactDB.listAll(ContactDB.class);
                handler.sendEmptyMessage(DB_READ);
            }
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(!Thread.interrupted()) {

            }
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

    public void pemissionGranted() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
            // Разрешения чтения контактов имеются
            Log.d(TAG, "Permission is granted");
            thread = new Thread(new contactRecive(mainActivity));
            thread.start();
        } else {
            // Разрешений нет
            Log.d(TAG, "Permission is not granted");

            // Запрос разрешений
            Log.d(TAG, "Request permissions");

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CONTACTS},PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.interrupt();
        tr.interrupt();
    }
}
class contactRecive implements Runnable {


    SoftReference<MainActivity> softReference;

    contactRecive(MainActivity mainActivity){
        softReference = new SoftReference(mainActivity);

    }

    @Override
    public void run() {
        if(!Thread.interrupted()) {
            MainActivity mainActivity = softReference.get();
            ReadContactPermission readContactPermission = new ReadContactPermission(mainActivity.activity, mainActivity.context, mainActivity);
            readContactPermission.readContacts(mainActivity.context);
        }
    }
}

class readContactDb implements Runnable {



    SoftReference<MainActivity> softReference;
    readContactDb(MainActivity mainActivity){
        softReference = new SoftReference(mainActivity);

    }

    @Override
    public void run() {
        if(!Thread.interrupted()) {
            MainActivity mainActivity = softReference.get();
            mainActivity.contactDBS = ContactDB.listAll(ContactDB.class);
            mainActivity.handler.sendEmptyMessage(mainActivity.DB_READ);
        }
    }
}