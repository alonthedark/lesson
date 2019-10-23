package com.example.lesson.presenter;

import com.example.lesson.views.ContactView;
import com.example.lesson.ModelDetail;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ContactView> {

    private ModelDetail model;
    private Disposable disposable;

    public ProfilePresenter() {

    }

    public void receiveContact(int id) {
        disposable = model.getContact(id).observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactDB -> getViewState().setData(contactDB));
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        model = new ModelDetail();
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }

}
