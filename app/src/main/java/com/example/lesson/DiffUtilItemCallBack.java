package com.example.lesson;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class DiffUtilItemCallBack extends DiffUtil.ItemCallback<ContactDB> {

    @Override
    public boolean areItemsTheSame(@NonNull ContactDB oldItem, @NonNull ContactDB newItem) {
        return oldItem.getIds().equals(newItem.getIds());
    }

    @Override
    public boolean areContentsTheSame(@NonNull ContactDB oldItem, @NonNull ContactDB newItem) {
        return oldItem.getName().equals(newItem.getName());
    }
}
