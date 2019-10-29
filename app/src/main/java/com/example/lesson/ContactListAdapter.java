package com.example.lesson;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lesson.database.ContactsDB;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.RecyclerView;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private static final String LOG_TAG = "Adapter";
    private LayoutInflater inflater;
    private OnClickListener onClickListener;
    private final AsyncListDiffer<ContactsDB> differ;

    // data is passed into the constructor
    ContactListAdapter(Context context, OnClickListener onClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
        DiffUtilItemCallBack diffUtilItemCallBack = new DiffUtilItemCallBack();
        differ = new AsyncListDiffer<>(this, diffUtilItemCallBack);

    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view, onClickListener, this);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactsDB contact = differ.getCurrentList().get(position);
        holder.bind(contact);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();

    }

    public void setData(List<ContactsDB> contactDBS) {
        differ.submitList(contactDBS);
    }

    // parent activity will implement this method to respond to click events
    public interface OnClickListener {
        void onItemClick(int id);
    }

    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView myTextView;
        private final OnClickListener onClickListener;
        private final ContactListAdapter adapter;

        ViewHolder(View itemView, OnClickListener onClickListener, ContactListAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            myTextView = itemView.findViewById(R.id.name);
            this.onClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        public void bind(ContactsDB contactDB){
            myTextView.setText(contactDB.getName());
        }

        @Override
        public void onClick(View view) {
            int posit = getAdapterPosition();
            if (posit != RecyclerView.NO_POSITION) {
                ContactsDB contact = adapter.differ.getCurrentList().get(posit);
                int ids = Integer.valueOf(contact.getIds());
                onClickListener.onItemClick(ids);
            } else {
                Log.i(LOG_TAG, "Get position no position");
            }
        }
    }
}