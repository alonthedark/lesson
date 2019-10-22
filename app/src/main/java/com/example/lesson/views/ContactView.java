package com.example.lesson.views;

import com.example.lesson.ContactDB;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ContactView extends MvpView {

    void setData(ContactDB contact);
}
