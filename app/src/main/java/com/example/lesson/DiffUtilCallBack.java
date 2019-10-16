package com.example.lesson;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffUtilCallBack extends DiffUtil.Callback {

    private List<ContactDB> oldContactDB;
    private List<ContactDB> newContactDB;

    DiffUtilCallBack(List<ContactDB> oldContactDB, List<ContactDB> newContactDB) {
        this.newContactDB = newContactDB;
        this.oldContactDB = oldContactDB;
    }

    @Override
    public int getOldListSize() {
        if (oldContactDB == null) {
            return 0;
        } else {
            return oldContactDB.size();
        }
    }


    @Override
    public int getNewListSize() {
        if (newContactDB == null) {
            return 0;
        } else {
            return newContactDB.size();
        }
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        ContactDB oldContact = oldContactDB.get(oldItemPosition);
        ContactDB newContact = newContactDB.get(newItemPosition);

        return oldContact.getIds().equals(newContact.getIds());

    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ContactDB oldContact = oldContactDB.get(oldItemPosition);
        ContactDB newContact = newContactDB.get(newItemPosition);
        return oldContact.getName().equals(newContact.getName());
    }
}
