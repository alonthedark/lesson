package com.example.lesson.di;

import com.example.lesson.database.AppDataBase;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

@Module
public class DataBaseModule {

    AppDataBase db;

    public DataBaseModule(@NonNull AppDataBase db){
        this.db = db;
    }

    @Provides
    @Singleton
    public AppDataBase dataBase() {
        return db;
    }

}
