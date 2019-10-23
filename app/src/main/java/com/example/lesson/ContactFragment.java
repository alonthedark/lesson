package com.example.lesson;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lesson.presenter.ProfilePresenter;
import com.example.lesson.views.ContactView;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class ContactFragment extends MvpAppCompatFragment implements ContactView {

    @InjectPresenter
    ProfilePresenter presenter;

    private ContactDB contactDBS;
    private static final String LOG_TAG = "ContactFragment";
    private static final String KEY_POSITION = "position";
    private ImageView avatar;
    private TextView name;
    private TextView phone;
    private TextView email;
    private int id;


    public ContactFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(KEY_POSITION);
        } else {
            Log.i(LOG_TAG, "getArgument is null");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.receiveContact(id);
    }

    @ProvidePresenter
    ProfilePresenter provideMainPresenter() {
        return new ProfilePresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        avatar = (ImageView) view.findViewById(R.id.avatar);
        name = (TextView) view.findViewById(R.id.name);
        phone = (TextView) view.findViewById(R.id.phone_number);
        email = (TextView) view.findViewById(R.id.email);

        return view;
    }


    @SuppressLint("SetTextI18n")
    public void setData(ContactDB contactDB) {
        contactDBS = contactDB;
        String nameText = contactDBS.getName();
        String phoneNumber = contactDBS.getPhone();
        String emailData = contactDBS.getEmail();
        name.setText("Имя " + nameText);
        phone.setText("Телефон " + phoneNumber);
        email.setText("Email " + emailData);
    }
}
