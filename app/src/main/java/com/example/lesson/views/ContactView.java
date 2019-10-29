package com.example.lesson.views;


import com.example.lesson.database.ContactsDB;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ContactView extends MvpView {

    void setData(ContactsDB contact);
}
