package com.example.lesson;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ModelDetail {

    public ModelDetail() {

    }

    public Observable<ContactDB> getContact(int id) {
        return Observable.fromCallable(() -> ContactDB.find(ContactDB.class, "ids = ?", String.valueOf(id)))
                .filter(contactDBList -> !contactDBList.isEmpty()).map(contactDBList -> contactDBList.get(0))
                .subscribeOn(Schedulers.io());
    }
}