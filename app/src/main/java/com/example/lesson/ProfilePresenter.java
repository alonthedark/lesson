package com.example.lesson;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ContactView> {

    private ContactDB contactDB;
    private ModelDetail model;
    private Disposable disposable;

    ProfilePresenter() {

    }

    public void receiveContact(int id) {
        disposable = model.getContact(id).observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactDB1 -> getViewState().setData(contactDB1.get(0)));
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        model = new ModelDetail();
        getViewState().receiveOneContact();
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }

}
