package com.example.lesson;

import java.util.List;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ContactView> implements CallBack {

    private ContactDB contactDB;
    private ModelDetail model;

    ProfilePresenter() {

    }

    public void receiveContact(int id) {
        model.receiveContact(this, id);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        model = new ModelDetail();
        getViewState().receiveOneContact();
    }

    @Override
    public void onDestroy() {
        model.cancel(this);
    }

    @Override
    public void onSuccess(List<ContactDB> contactDBList) {
        if (contactDBList.size() != 0) {
            contactDB = contactDBList.get(0);
            getViewState().setData(contactDB);
        }
    }

    @Override
    public void onError(Throwable error) {

    }
}
