package com.example.lesson.views;


import com.example.lesson.ContactDB;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.SkipStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface ListView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setAdapter(List<ContactDB> contactDBS);

    @StateStrategyType(SkipStrategy.class)
    void permissionGranted();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void startProgress();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideProgress();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setNewData(List<ContactDB> contactDBList);
}