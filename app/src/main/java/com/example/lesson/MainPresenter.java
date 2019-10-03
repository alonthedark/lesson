package com.example.lesson;

import android.content.Context;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class MainPresenter extends MvpPresenter<ListView> implements CallBack {

    private ContactModel model;
    private static final int CONTACTS_READ = 0;
    private Handler handler;
    private List<ContactDB> contactDBS;

    public MainPresenter() {
        this.model = new ContactModel();
        this.handler = new Handler(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().permissionGranted();
    }

    public void displayContacts(){
        getViewState().setAdapter(contactDBS);
    }

    public void readContacts(Context context) {
        model.startReadContacts(this, context);

    }
    @Override
    public void onDestroy() {
        model.cancel(this);
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onSuccess(List<ContactDB> contactDBList) {
        this.contactDBS = contactDBList;
        handler.sendEmptyMessage(CONTACTS_READ);
    }

    @Override
    public void onError(Throwable error) {

    }
    static class Handler extends android.os.Handler {

        WeakReference<MainPresenter> weakReference;

        Handler(MainPresenter presenter) {
            weakReference = new WeakReference<>(presenter);
        }

        public void handleMessage(@NonNull Message msg) {

            MainPresenter presenter = weakReference.get();
            if (presenter != null) {
                switch (msg.what) {
                    case CONTACTS_READ:
                        presenter.displayContacts();
                        break;
                }
            }
        }
    }
}