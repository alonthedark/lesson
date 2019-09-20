package com.example.lesson;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>{

    private List<String> mData;
    private List<Contact> contacts;
    private List<ContactDB> contactDBS = ContactDB.listAll(ContactDB.class);;
    private LayoutInflater mInflater;
    private OnCliclListner mOnCliclListner;

    // data is passed into the constructor
    ContactListAdapter(Context context,OnCliclListner onCliclListner) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnCliclListner = onCliclListner;
        //this.contacts = data;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view,mOnCliclListner);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ContactDB contact = contactDBS.get(position);
        holder.myTextView.setText(contact.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return contactDBS.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        OnCliclListner onCliclListner;

        ViewHolder(View itemView,OnCliclListner onCliclListner) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.name);
            this.onCliclListner = onCliclListner;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCliclListner.onItemClick(getAdapterPosition());
        }
    }

    // parent activity will implement this method to respond to click events
    public interface OnCliclListner{
        void onItemClick(int position);

    }
}