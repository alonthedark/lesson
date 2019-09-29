package com.my.mvp;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends FragmentActivity implements ContactListAdapter.OnClickListner {

    ContactListFragment contactListFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    private static final String TAG = "MainActivity";
    ContactFragment contactFragment;
    MainPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter();
        if (savedInstanceState == null) {
            attachContactList();
        }
    }

    @Override
    public void onItemClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        contactFragment = new ContactFragment();
        contactFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frag, contactFragment).addToBackStack(null).commit();
        Log.d(TAG, "CLICK " + position);

    }

    public void attachContactList() {
        contactListFragment = new ContactListFragment();
        fragmentManager.beginTransaction().add(R.id.frag, contactListFragment).commit();
    }


}