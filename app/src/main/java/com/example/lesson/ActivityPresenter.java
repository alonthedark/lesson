package com.example.lesson;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class ActivityPresenter extends MvpPresenter<ActivityView> {

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().attachContactList();
    }
    public void replaceFragment(){
        getViewState().attachContactFragment();
    }
}
