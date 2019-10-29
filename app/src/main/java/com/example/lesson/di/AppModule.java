package com.example.lesson.di;


import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application application;

    public AppModule(@NonNull Application context){
        this.application = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }
}
