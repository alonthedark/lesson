package com.example.lesson;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

import java.lang.ref.WeakReference;
import java.util.List;

public class ContactModel {

    private static final String TAG = "model contact";
    private Thread trReadDb;
    final static int CONTACT_READ = 0;
    private final static int DB_READ = 1;
    private List<ContactDB> contactDBList;
    Handler handler;
    private CallBack callBack;
    //MainPresenter presenter;

    ContactModel() {
        handler = new IncomingHandler(this);
    }

    public void startReadContacts(CallBack callback, Context context) {
        this.callBack = callback;
        Thread thContactReceive = new Thread(new ContactReceive(context, this));
        thContactReceive.start();
    }

    public void cancel(CallBack callback) {
        this.callBack = null;

    }

    static class ContactReceive implements Runnable {

        Context context;
        WeakReference<ContactModel> weakReference;

        ContactReceive(Context context, ContactModel contactModel) {
            this.context = context;
            weakReference = new WeakReference<>(contactModel);
        }

        @Override
        public void run() {
            if (!Thread.interrupted()) {
                ContactModel model = weakReference.get();
                if (model != null) {
                    ReadContact readContact = new ReadContact(model);
                    readContact.readContacts(context);
                }
            }
        }
    }

    static class ReadContactDb implements Runnable {

        WeakReference<ContactModel> weakReference;
        private CallBack cb;

        ReadContactDb(ContactModel contactModel, CallBack callBack) {
            weakReference = new WeakReference<>(contactModel);
            this.cb = callBack;
        }

        @Override
        public void run() {
            if (!Thread.interrupted()) {
                ContactModel model = weakReference.get();
                if (model != null) {
                    model.contactDBList = ContactDB.listAll(ContactDB.class);
                    cb.onSuccess(ContactDB.listAll(ContactDB.class));
                    model.handler.sendEmptyMessage(DB_READ);
                }
            }
        }
    }

    static class IncomingHandler extends Handler {

        WeakReference<ContactModel> weakReference;

        IncomingHandler(ContactModel model) {
            weakReference = new WeakReference<>(model);
        }

        public void handleMessage(@NonNull Message msg) {

            ContactModel model = weakReference.get();
            if (model != null) {
                switch (msg.what) {
                    case CONTACT_READ:
                        model.trReadDb = new Thread(new ReadContactDb(model, model.callBack));
                        model.trReadDb.start();
                        break;
                }
            }
        }
    }
}

