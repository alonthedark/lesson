package com.my.mvp;

public class FragmentPresenter {

    private FragmentView view;

    public void attachView(FragmentView view){
        this.view = view;
    }
    public void detachView() {
        view = null;
    }
}
