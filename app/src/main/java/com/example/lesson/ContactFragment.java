package com.example.lesson;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {

    private ContactDB contactDBS;
    private final static int DATA_READ = 1;
    private static final String LOG_TAG = "ContactFragment";
    private static final String KEY_POSITION = "position";
    private ImageView avatar;
    private TextView name;
    private TextView phone;
    private TextView email;
    private int id;
    private Thread thread;
    private Handler handl;

    public ContactFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handl = new IncomingHandler(this);
        if (getArguments() != null) {
            id = getArguments().getInt(KEY_POSITION);
        } else {
            Log.i(LOG_TAG, "getArgument is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        avatar = (ImageView) view.findViewById(R.id.avatar);
        name = (TextView) view.findViewById(R.id.name);
        phone = (TextView) view.findViewById(R.id.phone_number);
        email = (TextView) view.findViewById(R.id.email);
        thread = new Thread(new ReadDb(this));
        thread.start();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        String nameText = contactDBS.getName();
        String phoneNumber = contactDBS.getPhone();
        String emailData = contactDBS.getEmail();
        name.setText("Имя " + nameText);
        phone.setText("Телефон " + phoneNumber);
        email.setText("Email " + emailData);
    }

    @Override
    public void onDestroy() {
        handl.removeCallbacksAndMessages(null);
        super.onDestroy();
        thread.interrupt();
    }

    static class ReadDb implements Runnable {


        WeakReference<ContactFragment> weakReference;

        ReadDb(ContactFragment contactFrag) {
            weakReference = new WeakReference<>(contactFrag);

        }

        @Override
        public void run() {
            if (!Thread.interrupted()) {

                ContactFragment contactFragment = weakReference.get();
                if (contactFragment != null) {
                    int ids = contactFragment.id;
                    String idsArg = String.valueOf(ids);
                    List<ContactDB> contactDBS = ContactDB.find(ContactDB.class, "ids = ?", idsArg);
                    if (contactDBS.size() != 0) {
                        contactFragment.contactDBS = contactDBS.get(0);
                        contactFragment.handl.sendEmptyMessage(DATA_READ);
                    }
                }
            }
        }
    }

    static class IncomingHandler extends Handler {

        WeakReference<ContactFragment> weakReference;

        IncomingHandler(ContactFragment contactFragment) {
            weakReference = new WeakReference<>(contactFragment);
        }

        public void handleMessage(@NonNull Message msg) {

            ContactFragment contactFragment = weakReference.get();
            if (contactFragment != null) {
                switch (msg.what) {
                    case DATA_READ:
                        contactFragment.setData();
                        break;
                }
            }
        }
    }
}


