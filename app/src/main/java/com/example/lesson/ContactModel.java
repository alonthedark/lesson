package com.example.lesson;

import android.content.Context;

import com.example.lesson.database.AppDataBase;
import com.example.lesson.database.ContactDao;
import com.example.lesson.database.ContactsDB;
import com.example.lesson.di.AppDelegate;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ContactModel {

    private static final String TAG = "model contact";
    private ReadContact readContact;
    private ContactDao contactDao;

    @Inject
    AppDataBase db;

    public ContactModel() {
        AppDelegate.getAppComponent().inject(this);
        contactDao = db.contactDao();
    }

    public void startReadContacts() {
        readContact = new ReadContact();
    }

    public Observable<List<ContactsDB>> getFilteredContacts(String search) {
        if (search.isEmpty()) {
            return Observable.fromCallable(() -> contactDao.getAll())
                    .subscribeOn(Schedulers.io());
        } else {
            return Observable.fromCallable(() -> contactDao.searchList("%"+search+"%"))
                    .subscribeOn(Schedulers.io());
        }
    }

    public Observable<List<ContactsDB>> contactObservable() {
        return Observable.fromCallable(() -> readContact.readContacts())
                .concatMap(contacts -> {
                    saveDb(contacts);
                    return Observable.fromCallable(() -> contactDao.getAll());
                })
                .subscribeOn(Schedulers.io());
    }

    private void saveDb(List<Contact> list) {
        for (int i = 0; i < list.size(); i++) {
            ContactsDB contactsDB = new ContactsDB();
            contactsDB.id = 1;
            contactsDB.name = list.get(i).getName();
            contactsDB.phone = list.get(i).getPhone();
            contactsDB.email = list.get(i).getEmail();
            contactsDB.ids = list.get(i).getIds();
            contactDao.insert(contactsDB);
        }
    }
}

