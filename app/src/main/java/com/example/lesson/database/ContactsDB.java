package com.example.lesson.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class ContactsDB {

    @PrimaryKey
    @NonNull
    public String name;
    public String phone;
    public String ids;
    public String email;
    public long id;

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getIds() {
        return ids;
    }

    public String getEmail() {
        return email;
    }
}

