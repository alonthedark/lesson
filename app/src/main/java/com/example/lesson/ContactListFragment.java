package com.example.lesson;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class ContactListFragment extends MvpAppCompatFragment implements ListView, ContactListAdapter.OnClickListener {


    @InjectPresenter
    MainPresenter mainPresenter;

    private RecyclerView recyclerView;
    private static final String TAG = "contact list";
    private Context context;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    private ContactListAdapter adapter;
    private SearchView searchView;
    private SearchManager searchManager;
    private ProgressBar progressBar;
    private TextView loadContactInfo;

    public ContactListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        adapter = new ContactListAdapter(context, this);
        setHasOptionsMenu(true);

    }

    @ProvidePresenter
    MainPresenter provideMainPresenter() {
        return new MainPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        loadContactInfo = (TextView) view.findViewById(R.id.loadContactInfo);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setClickable(true);
        searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) view.findViewById(R.id.contact_search);
        return view;
    }

    public void setAdapter(List<ContactDB> contactDBS) {
        Log.d(TAG, "adapter");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(Objects.requireNonNull(getActivity()).getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainPresenter.searchContact(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mainPresenter.searchContact(query);
                return false;
            }
        });
        adapter.setData(contactDBS);
        recyclerView.addItemDecoration(new ItemDecoration(context, DividerItemDecoration.VERTICAL, 30));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        ContactFragment contactFragment = new ContactFragment();
        contactFragment.setArguments(bundle);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                .beginTransaction().replace(R.id.frag, contactFragment).addToBackStack(null).commit();

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

    public void startProgress() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        loadContactInfo.setVisibility(TextView.VISIBLE);

    }

    public void hideProgress() {
        searchView.setVisibility(SearchView.VISIBLE);
        progressBar.setVisibility(ProgressBar.GONE);
        loadContactInfo.setVisibility(TextView.GONE);
    }

    public void setNewData(List<ContactDB> contactDBList) {
        adapter.setResult(contactDBList);
    }
}
