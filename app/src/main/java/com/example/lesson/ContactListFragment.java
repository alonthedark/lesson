package com.example.lesson;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContactListFragment extends Fragment implements ContactListAdapter.OnCliclListner{

    Handler handler;
    private ContactListFragment contactListFragment = this;
    final static int CONTACT_READ = 0;
    private final static int DB_READ = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    private static final String TAG = "ContactList";
    private List<ContactDB> contactDBList;
    private Context context;
    private Activity activity;
    private RecyclerView recyclerView;
    private Thread thContactReceive;
    private Thread trReadDb;
    private boolean resume = false;


    ContactListFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getActivity();
        if (savedInstanceState == null) {
            permissionGranted();
        }
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CONTACT_READ:
                        trReadDb = new Thread(new ReadContactDb(contactListFragment));
                        trReadDb.start();
                        break;
                    case DB_READ:
                        setAdapter(recyclerView,contactDBList);
                        resume = true;
                        break;
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setClickable(true);
        if(resume){
            setAdapter(recyclerView,contactDBList);
        }

        return view;
    }

    private void permissionGranted() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // Разрешения чтения контактов имеются
            Log.d(TAG, "Permission is granted");
            thContactReceive = new Thread(new ContactReceive(contactListFragment));
            thContactReceive.start();
        } else {
            // Разрешений нет
            Log.d(TAG, "Permission is not granted");

            // Запрос разрешений
            Log.d(TAG, "Request permissions");

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            permissionGranted();

        }
    }
    @Override
    public void onItemClick(int position) {
        ((MainActivity) Objects.requireNonNull(getActivity())).onItemClick(position);
    }

    private void setAdapter(RecyclerView recycler, List<ContactDB> contactDBS) {
        Log.d(TAG, "adapter");
        ContactListAdapter adapter = new ContactListAdapter(context, this, contactDBS);
        recycler.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
        thContactReceive.interrupt();
        trReadDb.interrupt();
    }

    static class ContactReceive implements Runnable {

        WeakReference<ContactListFragment> weakReference;

        ContactReceive(ContactListFragment contactListFragment) {
            weakReference = new WeakReference(contactListFragment);
        }

        @Override
        public void run() {
            if (!Thread.interrupted()) {
                ContactListFragment contactListFragment = weakReference.get();
                if (contactListFragment != null) {
                    ReadContact readContact = new ReadContact(contactListFragment.activity, contactListFragment.context, contactListFragment.contactListFragment);
                    readContact.readContacts(contactListFragment.context);
                }
            }
        }
    }

    static class ReadContactDb implements Runnable {

        WeakReference<ContactListFragment> weakReference;

        ReadContactDb(ContactListFragment contactListFragment) {
            weakReference = new WeakReference(contactListFragment);
        }

        @Override
        public void run() {
            if (!Thread.interrupted()) {
                ContactListFragment contactListFragment = weakReference.get();
                if (contactListFragment != null) {
                    contactListFragment.contactDBList = ContactDB.listAll(ContactDB.class);
                    contactListFragment.handler.sendEmptyMessage(contactListFragment.DB_READ);
                }
            }
        }
    }
}
