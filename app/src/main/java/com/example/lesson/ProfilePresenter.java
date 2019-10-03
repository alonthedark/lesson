package com.example.lesson;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.util.List;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ContactView> {

    private ContactDB contactDB;
    private int id;

    ProfilePresenter() {

    }
    public void receiveContact(int id){
        this.id = id;
        ContactTask contactTask = new ContactTask();
        contactTask.execute();

    }

    @Override
    protected void onFirstViewAttach(){
        super.onFirstViewAttach();
        getViewState().receiveOneContact();
    }

    @SuppressLint("StaticFieldLeak")
    class ContactTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            int ids = id;
            String idsArg = String.valueOf(ids);
            List<ContactDB> contactDBS = ContactDB.find(ContactDB.class, "ids = ?", idsArg);
            if (contactDBS.size() != 0) {
                contactDB = contactDBS.get(0);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            getViewState().setData(contactDB);
        }
    }


}
