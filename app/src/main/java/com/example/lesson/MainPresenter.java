package com.example.lesson;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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

    public void displayContacts(List<Contact> list) {

        Observer<Contact> observer = new Observer<Contact>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Contact contact) {
                model.saveContactDBS(contact);
            }


            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                disposable = model.getContactDbList().observeOn(AndroidSchedulers.mainThread())
                        .subscribe(contactDBList -> getViewState().setAdapter(contactDBList));
            }
        };
        model.saveContactDb(list).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }


    public void readContacts(Context context) {
        model.startReadContacts(context);
        disposable = model.contactObservable(context).observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> displayContacts(list));
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }

}