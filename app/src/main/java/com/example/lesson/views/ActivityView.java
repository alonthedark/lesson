package com.example.lesson.views;

import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface ActivityView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void attachContactList();

}