package com.example.lesson;

import android.content.Context;
import android.os.Bundle;
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
    ImageView avatar;
    TextView name;
    TextView phone;
    TextView email;
    int id;
    private LayoutInflater mInflater;
    LinearLayout mLinearLayout;
    String LOG_TAG = "ContactFragment";
    String keyPosition = "position";

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
        Thread thread = new Thread(runnable);
        thread.start();
        return view;
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            contactDBS = ContactDB.listAll(ContactDB.class);
            //contactDBS = ContactDB.find(ContactDB.class,"ids = ?",String.valueOf(id));
            setData();
        }
    };
    private void setData(){
        ContactDB contact = contactDBS.get(id);
        String nameText = contact.getName();
        String phoneNumber = contact.getPhone();
        String emailData = contact.getEmail();
        name.setText("Имя "+nameText);
        phone.setText("Телефон "+phoneNumber);
        email.setText("Email "+emailData);
    }

}
