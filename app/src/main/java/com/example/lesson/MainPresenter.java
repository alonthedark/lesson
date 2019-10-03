package com.example.lesson;

import android.content.Context;

import java.util.List;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class MainPresenter extends MvpPresenter<ListView> {

    private ContactModel model;

    public MainPresenter() {
        this.model = new ContactModel(this);
    }

    @Override
    protected void onFirstViewAttach(){
        super.onFirstViewAttach();
        getViewState().permissionGranted();
    }

    public void readContacts(Context context){
        model.startReadContacts(context);
    }

    public void displeyContactList(List<ContactDB> contactDBS){
        getViewState().setAdapter(contactDBS);
    }

}