package com.my.mvp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class ContactListFragment extends Fragment implements ContactListView, ContactListAdapter.OnClickListner {

    private RecyclerView recyclerView;
    private static final String TAG = "contact list";
    private Context context;
    private MainPresenter presenter;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    private ContactListFragment contactListFragment = this;
    private ContactListAdapter adapter;


    public ContactListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        presenter = new MainPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setClickable(true);
        presenter.attachContactListView(contactListFragment);
        if (savedInstanceState == null) {
            permissionGranted();
        }

        return view;
    }

    public void setAdapter(List<ContactDB> contactDBS) {
        Log.d(TAG, "adapter");
        adapter = new ContactListAdapter(context, this, contactDBS);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        ((MainActivity) Objects.requireNonNull(getActivity())).onItemClick(position);
    }

    private void permissionGranted() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // Разрешения чтения контактов имеются
            Log.d(TAG, "Permission is granted");
            presenter.readContacts(context);
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
                    presenter.readContacts(context);
                } else {
                    // permission denied
                    Log.d(TAG, "Permission is not granted");
                }
        }
    }
}
