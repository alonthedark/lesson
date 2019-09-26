package com.example.lesson;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends FragmentActivity implements ContactListAdapter.OnCliclListner {


    ContactListFragment contactListFragment;
    ContactFragment contactFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTransaction();
    }

    private void startTransaction() {
        contactListFragment = new ContactListFragment();
        fragmentManager.beginTransaction().addToBackStack(null).add(R.id.frag, contactListFragment).commit();
    }

    public void setAdapter(RecyclerView recycler, List<ContactDB> contactDBS) {

        Log.d(TAG, "adapter");
        ContactListAdapter adapter = new ContactListAdapter(this, this, contactDBS);
        recycler.setAdapter(adapter);
        recycler.getAdapter().notifyDataSetChanged();
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

