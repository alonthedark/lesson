package com.example.lesson;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.util.List;

public class ModelDetail {


    private int id;
    private List<ContactDB> contactDBS;
    private CallBack callBack;

    ModelDetail() {

    }

    public void receiveContact(CallBack callBack, int id) {
        this.id = id;
        this.callBack = callBack;
        ContactTask contactTask = new ContactTask();
        contactTask.execute();
    }

    public void cancel(CallBack callback) {
        this.callBack = null;

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
            contactDBS = ContactDB.find(ContactDB.class, "ids = ?", idsArg);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            callBack.onSuccess(contactDBS);
        }
    }
}
