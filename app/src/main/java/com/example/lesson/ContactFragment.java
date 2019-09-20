package com.example.lesson;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    ContactFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        avatar =(ImageView) view.findViewById(R.id.avatar);
        name = (TextView) view.findViewById(R.id.name);
        phone = (TextView) view.findViewById(R.id.phone_number);
        email = (TextView) view.findViewById(R.id.email);
        int id = getArguments().getInt("position");
        contactDBS = ContactDB.listAll(ContactDB.class);
        ContactDB contact = contactDBS.get(id);
        String nameText = contact.getName();
        String phoneNumber = contact.getPhone();
        name.setText("Имя "+nameText);
        phone.setText("Телефон "+phoneNumber);
        email.setText("Email ");

        return view;
    }
}
