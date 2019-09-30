package com.example.lesson;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

public class ContactModel {

    private static final String TAG = "model contact";
    private Thread trReadDb;
    private Thread contactReceive;
    final static int CONTACT_READ = 0;
    private final static int DB_READ = 1;
    private final static int CONTACT_RECIVED = 2;
    private List<ContactDB> contactDBList;
    private ContactDB contactDB;
    private int id;
    Handler handler;
    private MainPresenter presenter;

    ContactModel() {
        handler = new IncomingHandler(this);
    }

    public void startReadContacts(Context context, MainPresenter mPresenter) {
        this.presenter = mPresenter;
        Thread thContactReceive = new Thread(new ContactReceive(context,this));
        thContactReceive.start();
    }

    public void contactRead(){
        presenter.displeyContactList(contactDBList);
    }

    public void contactReceived(){
        presenter.showContact(contactDB);
    }

    public void receiveContact(MainPresenter presenter, int id){
        this.presenter = presenter;
        contactReceive = new Thread(new ReadOneContadt(this,id));
        contactReceive.start();
    }

    static class ContactReceive implements Runnable {

        Context context;
        WeakReference<ContactModel> weakReference;

        ContactReceive(Context context,ContactModel contactModel) {
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

        ReadContactDb(ContactModel contactModel) {
            weakReference = new WeakReference<>(contactModel);
        }

        @Override
        public void run() {
            if (!Thread.interrupted()) {
                ContactModel model = weakReference.get();
                if (model != null) {
                    model.contactDBList = ContactDB.listAll(ContactDB.class);
                    model.handler.sendEmptyMessage(DB_READ);
                }
            }
        }
    }

    static class ReadOneContadt implements Runnable {

        WeakReference<ContactModel> weakReference;
        int id;

        ReadOneContadt(ContactModel contactModel,int id) {
            weakReference = new WeakReference<>(contactModel);
            this.id = id;

        }

        @Override
        public void run() {
            if (!Thread.interrupted()) {

                ContactModel contactModel = weakReference.get();
                if (contactModel != null) {
                    int ids = id;
                    String idsArg = String.valueOf(ids);
                    List<ContactDB> contactDBS = ContactDB.find(ContactDB.class, "ids = ?", idsArg);
                    if (contactDBS.size() != 0) {
                        contactModel.contactDB = contactDBS.get(0);
                        contactModel.handler.sendEmptyMessage(CONTACT_RECIVED);
                    }
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
                        model.trReadDb = new Thread(new ReadContactDb(model));
                        model.trReadDb.start();
                        break;
                    case DB_READ:
                        model.contactRead();
                        break;
                    case CONTACT_RECIVED:
                        model.contactReceived();
                        break;
                }
            }
        }
    }
}

