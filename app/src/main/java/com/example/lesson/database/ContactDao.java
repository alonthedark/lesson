package com.example.lesson.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contactsdb")
    List<ContactsDB> getAll();

    @Query("SELECT * FROM contactsdb WHERE id = :id")
    ContactsDB getById(long id);

    @Query("SELECT * FROM contactsdb WHERE name LIKE  :search")
    List<ContactsDB> searchList(String search);

    @Query("SELECT * FROM contactsdb WHERE ids = :ids")
    ContactsDB getByIds(String ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ContactsDB contactsDB);

    @Update
    void update(ContactsDB contactsDB);

    @Delete
    void delete(ContactsDB contactsDB);

}