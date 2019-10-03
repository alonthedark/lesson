package com.example.lesson;

import java.util.List;

interface CallBack {
    void onSuccess(List<ContactDB> contactDBList);

    void onError(Throwable error);
}