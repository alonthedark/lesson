package com.example.lesson;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

public class ContactFragment extends Fragment {

    Context context;
    List<ContactDB> contactDBS;
    final static int DATA_READ = 1;
    ImageView avatar;
    TextView name;
    TextView phone;
    TextView email;
    int id;
    private LayoutInflater mInflater;
    LinearLayout mLinearLayout;
    String LOG_TAG = "ContactFragment";
    String keyPosition = "position";
    Thread thread;
    Handler handl;

    ContactFragment(){



    }
    public static ContactFragment newInstance(int position) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = fragment.getArguments();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        handl = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DATA_READ:
                        setData();
                        break;

                }
            }
        };
        if (getArguments() != null) {
            id = getArguments().getInt(keyPosition);
        }
        else {
            Log.i(LOG_TAG,"getArgument is null");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        avatar =(ImageView) view.findViewById(R.id.avatar);
        name = (TextView) view.findViewById(R.id.name);
        phone = (TextView) view.findViewById(R.id.phone_number);
        email = (TextView) view.findViewById(R.id.email);
        thread = new Thread(new readDB(this));
        thread.start();
        return view;
    }

    private void setData(){
        ContactDB contact = contactDBS.get(id);
        String nameText = contact.getName();
        String phoneNumber = contact.getPhone();
        String emailData = contact.getEmail();
        name.setText("Имя "+nameText);
        phone.setText("Телефон "+phoneNumber);
        email.setText("Email "+emailData);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }
}
class readDB implements Runnable{

    ContactFragment contactFragment;
    readDB(ContactFragment contactFrag){
        this.contactFragment = contactFrag;
    }

    @Override
    public void run() {
        if(!Thread.interrupted()) {
            contactFragment.contactDBS = ContactDB.listAll(ContactDB.class);
            contactFragment.handl.sendEmptyMessage(contactFragment.DATA_READ);
        }
    }
}
