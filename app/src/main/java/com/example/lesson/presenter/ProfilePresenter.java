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
    private int id;

    public ProfilePresenter() {

    }

    public void setData(int id) {
        this.id = id;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        model = new ModelDetail();
        disposable = model.getContact(id).observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactDB -> getViewState().setData(contactDB));
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }

}
