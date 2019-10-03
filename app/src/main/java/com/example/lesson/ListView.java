package com.example.lesson;


import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ListView extends MvpView {
    void setAdapter(List<ContactDB> contactDBS);
    void permissionGranted();
}