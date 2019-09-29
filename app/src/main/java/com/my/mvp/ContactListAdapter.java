package com.my.mvp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private static final String LOG_TAG = "Adapter";
    private List<ContactDB> contactDBS;
    private LayoutInflater mInflater;
    private OnClickListner mOnClickListner;
    private ContactDB contact;

    // data is passed into the constructor
    ContactListAdapter(Context context, OnClickListner onClickListner, List<ContactDB> contactDBS) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnClickListner = onClickListner;
        this.contactDBS = contactDBS;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view, mOnClickListner);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        contact = contactDBS.get(position);
        holder.myTextView.setText(contact.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return contactDBS.size();
    }


    // parent activity will implement this method to respond to click events
    public interface OnClickListner {
        void onItemClick(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        OnClickListner onClickListner;

        ViewHolder(View itemView, OnClickListner onClickListner) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.name);
            this.onClickListner = onClickListner;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int posit = getAdapterPosition();
            if (posit != RecyclerView.NO_POSITION) {
                onClickListner.onItemClick(getAdapterPosition());
            } else {
                Log.i(LOG_TAG, "Get position no position");
            }
        }
    }
}