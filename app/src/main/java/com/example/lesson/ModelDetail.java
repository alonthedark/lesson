package com.example.lesson;

import com.example.lesson.database.AppDataBase;
import com.example.lesson.database.ContactDao;
import com.example.lesson.database.ContactsDB;
import com.example.lesson.di.AppDelegate;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


public class ModelDetail {

    private ContactDao contactDao;

    @Inject
    AppDataBase db;

    public ModelDetail(){
        AppDelegate.getAppComponent().inject(this);
        contactDao = db.contactDao();
    }

    public Observable<ContactsDB> getContact(int id) {
        return Observable.fromCallable(() -> contactDao.getByIds(String.valueOf(id)))
                .subscribeOn(Schedulers.io());
    }
}