package com.example.lesson;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends FragmentActivity implements ContactListAdapter.OnCliclListner {

    ContactListFragment contactListFragment;
    ContactFragment contactFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTransaction();
    }

    private void startTransaction() {
        contactListFragment = new ContactListFragment(this);
        fragmentManager.beginTransaction().add(R.id.frag, contactListFragment).commit();
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
}

