package com.example.lesson;

import android.os.Bundle;

import com.example.lesson.presenter.ActivityPresenter;
import com.example.lesson.views.ActivityView;

import androidx.fragment.app.FragmentManager;
import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;


public class MainActivity extends MvpAppCompatActivity implements ActivityView {

    @InjectPresenter
    ActivityPresenter activityPresenter;

    private static final String TAG = "MainActivity";

    @ProvidePresenter
    ActivityPresenter provideActivityPresenter() {
        return new ActivityPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void attachContactList() {
        ContactListFragment contactListFragment = new ContactListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frag, contactListFragment).commit();
    }
}
