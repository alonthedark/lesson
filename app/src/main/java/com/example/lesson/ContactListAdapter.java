package com.example.lesson;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.RecyclerView;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private static final String LOG_TAG = "Adapter";
    private LayoutInflater mInflater;
    private OnClickListener mOnClickListner;
    private ContactDB contact;
    private ContactListAdapter adapter = this;
    AsyncListDiffer<ContactDB> differ;

    // data is passed into the constructor
    ContactListAdapter(Context context, OnClickListener onClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnClickListner = onClickListener;
        DiffUtilItemCallBack diffUtilItemCallBack = new DiffUtilItemCallBack();
        differ = new AsyncListDiffer<>(this, diffUtilItemCallBack);

    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view, mOnClickListner, this);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        contact = differ.getCurrentList().get(position);
        holder.myTextView.setText(contact.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();

    }

    public void setData(List<ContactDB> contactDBS) {
        differ.submitList(contactDBS);
    }

    public void setResult(List<ContactDB> newList) {
        differ.submitList(newList);
    }

    // parent activity will implement this method to respond to click events
    public interface OnClickListener {
        void onItemClick(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        OnClickListener onClickListener;
        ContactListAdapter adapter;

        ViewHolder(View itemView, OnClickListener onClickListener, ContactListAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            myTextView = itemView.findViewById(R.id.name);
            this.onClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int posit = getAdapterPosition();
            if (posit != RecyclerView.NO_POSITION) {
                ContactDB contact = adapter.differ.getCurrentList().get(posit);
                int ids = Integer.valueOf(contact.getIds());
                onClickListener.onItemClick(ids);
            } else {
                Log.i(LOG_TAG, "Get position no position");
            }
        }
    }
}