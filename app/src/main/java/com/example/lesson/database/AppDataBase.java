package com.example.lesson.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ContactsDB.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract ContactDao contactDao();

}