package com.example.lesson;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;


public class MainActivity extends MvpAppCompatActivity implements ActivityView, ContactListAdapter.OnClickListener {

    @InjectPresenter
    ActivityPresenter activityPresenter;
    ContactListFragment contactListFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    private static final String TAG = "MainActivity";
    ContactFragment contactFragment;
    int position;

    @ProvidePresenter
    ActivityPresenter provideActivityPresenter() {
        ActivityPresenter presenter = new ActivityPresenter();
        return new ActivityPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onItemClick(int position) {
        this.position = position;
        activityPresenter.replaceFragment();
    }


    public void attachContactFragment() {
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
