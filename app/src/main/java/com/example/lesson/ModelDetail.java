package com.example.lesson;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ModelDetail {


    private int id;
    private List<ContactDB> contactDBS;

    ModelDetail() {

    }

    Observable<List<ContactDB>> getContact(int id){
        return Observable.fromCallable(() -> ContactDB.find(ContactDB.class, "ids = ?", String.valueOf(id)))
                .subscribeOn(Schedulers.io());
    }
}
