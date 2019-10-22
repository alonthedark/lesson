package com.example.lesson.presenter;

import com.example.lesson.views.ActivityView;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class ActivityPresenter extends MvpPresenter<ActivityView> {

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().attachContactList();
    }
}
