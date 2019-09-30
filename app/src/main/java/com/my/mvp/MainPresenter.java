package com.my.mvp;

import android.content.Context;
import android.util.Log;

import java.util.List;

public class MainPresenter {

    private static final String TAG = "Presenter";
    private FragmentView fragView;
    private ContactListView contactListView;
    private ContactModel model;

    public MainPresenter(){

        Log.d(TAG, "Constructor");
    }

    public void attachContactView(FragmentView fragView){
        this.fragView = fragView;
        this.model = new ContactModel();
    }

    public void detachContactView(){
        this.fragView = null;
    }

    public void attachContactListView(ContactListView contactListView){
        this.contactListView = contactListView;
        this.model = new ContactModel();
    }

    public void detachContactListView(){
        this.contactListView = null;
    }

    public void reciveOneContact(int id){
        model.receiveContact(this, id);
    }

    public void displeyContactList(List<ContactDB> contactDBS){
        contactListView.setAdapter(contactDBS);
    }

    public void readContacts(Context context){
        model.startReadContacts(context,this);
    }

    public void showContact(ContactDB contactDB){
        fragView.setData(contactDB);
    }

}
