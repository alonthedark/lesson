package com.example.lesson;

import com.example.lesson.database.ContactsDB;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class DiffUtilItemCallBack extends DiffUtil.ItemCallback<ContactsDB> {

    @Override
    public boolean areItemsTheSame(@NonNull ContactsDB oldItem, @NonNull ContactsDB newItem) {
        return oldItem.getIds().equals(newItem.getIds());
    }

    @Override
    public boolean areContentsTheSame(@NonNull ContactsDB oldItem, @NonNull ContactsDB newItem) {
        return oldItem.getName().equals(newItem.getName());
    }
}
