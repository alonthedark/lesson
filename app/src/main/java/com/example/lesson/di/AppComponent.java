package com.example.lesson.di;


import com.example.lesson.ContactListFragment;
import com.example.lesson.ContactModel;
import com.example.lesson.ModelDetail;
import com.example.lesson.ReadContact;

import javax.inject.Singleton;

import dagger.Component;



@Component(modules = {AppModule.class, DataBaseModule.class})
@Singleton
public interface AppComponent {

    void inject (ContactListFragment contactListFragment);
    void inject (ContactModel contactModel);
    void inject (ModelDetail modelDetail);
    void inject (ReadContact readContact);
}
