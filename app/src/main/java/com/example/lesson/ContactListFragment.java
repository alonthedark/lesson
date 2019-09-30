package com.example.lesson;

import android.Manifest;
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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContactListFragment extends Fragment implements ContactListAdapter.OnClickListner {

    Handler handler;
    private ContactListFragment contactListFragment = this;
    final static int CONTACT_READ = 0;
    private final static int DB_READ = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    private static final String TAG = "ContactList";
    private List<ContactDB> contactDBList;
    private Context context;
    private RecyclerView recyclerView;
    private Thread thContactReceive;
    private Thread trReadDb;
    private ContactListAdapter adapter;

    public ContactListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        handler = new IncomingHandler(contactListFragment);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setClickable(true);
        permissionGranted();
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

            requestPermissions(new String[]{
                    Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS:
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    thContactReceive = new Thread(new ContactReceive(contactListFragment));
                    thContactReceive.start();
                } else {
                    // permission denied
                    Log.d(TAG, "Permission is not granted");
                }
        }
    }

    @Override
    public void onItemClick(int position) {
        ((MainActivity) Objects.requireNonNull(getActivity())).onItemClick(position);
    }

    private void setAdapter(RecyclerView recycler, List<ContactDB> contactDBS) {
        Log.d(TAG, "adapter");
        adapter = new ContactListAdapter(context, this, contactDBS);
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
            weakReference = new WeakReference<>(contactListFragment);
        }

        @Override
        public void run() {
            if (!Thread.interrupted()) {
                ContactListFragment fragment = weakReference.get();
                if (fragment != null) {
                    ReadContact readContact = new ReadContact(fragment.contactListFragment);
                    readContact.readContacts(fragment.context);
                }
            }
        }
    }

    static class ReadContactDb implements Runnable {

        WeakReference<ContactListFragment> weakReference;

        ReadContactDb(ContactListFragment contactListFragment) {
            weakReference = new WeakReference<>(contactListFragment);
        }

        @Override
        public void run() {
            if (!Thread.interrupted()) {
                ContactListFragment fragment = weakReference.get();
                if (fragment != null) {
                    fragment.contactDBList = ContactDB.listAll(ContactDB.class);
                    fragment.handler.sendEmptyMessage(DB_READ);
                }
            }
        }
    }

    static class IncomingHandler extends Handler {

        WeakReference<ContactListFragment> weakReference;

        IncomingHandler(ContactListFragment contactListFragment) {
            weakReference = new WeakReference<>(contactListFragment);
        }

        public void handleMessage(@NonNull Message msg) {

            ContactListFragment fragment = weakReference.get();
            if (fragment != null) {
                switch (msg.what) {
                    case CONTACT_READ:
                        fragment.trReadDb = new Thread(new ReadContactDb(fragment));
                        fragment.trReadDb.start();
                        break;
                    case DB_READ:
                        fragment.setAdapter(fragment.recyclerView, fragment.contactDBList);
                        break;
                }
            }
        }
    }
}
