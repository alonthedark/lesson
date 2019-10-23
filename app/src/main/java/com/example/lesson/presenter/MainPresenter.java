package com.example.lesson.presenter;

import android.content.Context;

import com.example.lesson.ContactModel;
import com.example.lesson.views.ListView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class MainPresenter extends MvpPresenter<ListView> {


    private static final String TAG = "MainPresenter";
    private ContactModel model;
    private Disposable disposable;

    public MainPresenter() {
        this.model = new ContactModel();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().permissionGranted();
    }

    public void readContacts(Context context) {
        model.startReadContacts();
        disposable = model.contactObservable(context)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> getViewState().startProgress())
                .doOnTerminate(() -> getViewState().hideProgress())
                .subscribe(contactDBS -> getViewState().setAdapter(contactDBS));
    }

    public void searchContact(String search) {
        if (!disposable.isDisposed() && disposable!=null) {
            disposable.dispose();
        }
        disposable = model.getFilteredContacts(search).observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactDBS -> getViewState().setNewData(contactDBS));
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }

}