package com.example.lesson;

import android.content.Context;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class MainPresenter extends MvpPresenter<ListView> {


    private static final String TAG = "MainPresenter";
    private ContactModel model;
    private Disposable disposable;
    Context context;

    public MainPresenter() {
        this.model = new ContactModel();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().permissionGranted();
    }

    public void readContacts(Context context) {
        this.context = context;
        model.startReadContacts(context);
        disposable = model.contactObservable(context)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> getViewState().startProgress())
                .doOnTerminate(() -> getViewState().hideProgress())
                .subscribe(contactDBS -> getViewState().setAdapter(contactDBS));
    }

    public void searchContact(String search){
        disposable =  model.getFilteredContacts(search).observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactDBS -> getViewState().setNewData(contactDBS));
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }

}