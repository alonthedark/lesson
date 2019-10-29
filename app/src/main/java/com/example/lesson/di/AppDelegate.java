package com.example.lesson.di;

import androidx.room.Room;

import com.example.lesson.database.AppDataBase;

public class AppDelegate extends android.app.Application {

    private static AppComponent appComponent;

    private static AppDataBase db;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static AppDataBase getDataBase(){
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db =  buildDataBase();
        appComponent = buildComponent();
    }

    protected AppDataBase buildDataBase(){
        return Room.databaseBuilder(this, AppDataBase.class, "database")
                .build();
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataBaseModule(new DataBaseModule(db))
                .build();
    }
}
