package com.example.lesson;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class ContactListFragment extends MvpAppCompatFragment implements ListView, ContactListAdapter.OnClickListner {


    @InjectPresenter
    MainPresenter mainPresenter;

    private RecyclerView recyclerView;
    private static final String TAG = "contact list";
    private Context context;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    private ContactListAdapter adapter;
    private List<ContactDB> contactDBList;

    public ContactListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }
    @ProvidePresenter
    MainPresenter provideMainPresenter(){
        return new MainPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setClickable(true);
        return view;
    }

    public void setAdapter(List<ContactDB> contactDBS) {
        Log.d(TAG, "adapter");
        this.contactDBList = contactDBS;
        adapter = new ContactListAdapter(context, this, contactDBList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainPresenter.detachView(this);
    }

    @Override
    public void onItemClick(int position) {
        ((MainActivity) Objects.requireNonNull(getActivity())).onItemClick(position);
    }

    public void permissionGranted() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // Разрешения чтения контактов имеются
            Log.d(TAG, "Permission is granted");
            mainPresenter.readContacts(context);

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
                    mainPresenter.readContacts(context);
                } else {
                    // permission denied
                    Log.d(TAG, "Permission is not granted");
                }
                break;
        }
    }

}
