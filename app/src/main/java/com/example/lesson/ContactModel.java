package com.example.lesson;

import android.content.Context;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ContactModel {

    private static final String TAG = "model contact";
    private ReadContact readContact;

    ContactModel() {

    }

    public void startReadContacts() {
        readContact = new ReadContact();
    }

    Observable<List<ContactDB>> getFilteredContacts(String search) {
        if (search.isEmpty()) {
            return Observable.fromCallable(() -> ContactDB.listAll(ContactDB.class))
                    .subscribeOn(Schedulers.io());
        } else {
            return Observable.fromCallable(() -> ContactDB
                    .findWithQuery(ContactDB.class, "Select * from CONTACT_DB where name LIKE ?", "%" + search + "%"))
                    .subscribeOn(Schedulers.io());

        }
    }

    Observable<List<ContactDB>> contactObservable(Context context) {
        return Observable.fromCallable(() -> readContact.readContacts(context))
                .concatMap(contacts -> {
                    saveDb(contacts);
                    return Observable
                            .fromCallable(() -> ContactDB.listAll(ContactDB.class));
                })
                .subscribeOn(Schedulers.io());
    }

    private void saveDb(List<Contact> list) {
        for (int i = 0; i < list.size(); i++) {
            ContactDB contactDB = new ContactDB(
                    list.get(i).getName(),
                    list.get(i).getPhone(),
                    list.get(i).getIds(),
                    list.get(i).getEmail());
            contactDB.save();
        }
    }
}

