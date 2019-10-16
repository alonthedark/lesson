package com.example.lesson;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

import java.lang.ref.WeakReference;
import java.util.List;

public class ContactModel {

    private static final String TAG = "model contact";
    private List<Contact> contactList;
    private List<ContactDB> contactDBList;
    private Context context;
    Disposable disposable;
    ReadContact readContact;

    ContactModel() {
    }

    public void startReadContacts(Context context) {
        readContact = new ReadContact(context);
    }

    public void saveDataBase(List<Contact> list){
        disposable = saveContactDb(list).subscribe(contact -> saveContactDBS(contact));
    }

    Observable<List<Contact>> contactObservable(Context context){
        return Observable.just(readContact.readContacts(context)).subscribeOn(Schedulers.computation());
    }


    Observable<Contact> saveContactDb(List<Contact> list){
        return Observable.fromIterable(list).subscribeOn(Schedulers.io());
    }

    public void saveContactDBS(Contact contact){
        ContactDB contactDB = new ContactDB(
                contact.getName(),
                contact.getPhone(),
                contact.getIds(),
                contact.getEmail());
        contactDB.save();
    }

    Single<List<ContactDB>> getContactDbList(){
        return Single.just(ContactDB.listAll(ContactDB.class))
                .subscribeOn(Schedulers.io());
    }

}

