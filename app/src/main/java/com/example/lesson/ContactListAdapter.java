package com.example.lesson;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> implements Filterable {

    private static final String LOG_TAG = "Adapter";
    private List<ContactDB> contactDBS;
    public List<ContactDB> displayedList;
    private List<ContactDB> mFilteredList;
    private LayoutInflater mInflater;
    private OnClickListener mOnClickListner;
    private ContactDB contact;
    private ContactListAdapter adapter = this;

    // data is passed into the constructor
    ContactListAdapter(Context context, OnClickListener onClickListener, List<ContactDB> contactDBS) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnClickListner = onClickListener;
        this.contactDBS = contactDBS;
        this.displayedList = this.contactDBS;

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

        contact = displayedList.get(position);
        holder.myTextView.setText(contact.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        if (displayedList != null) {
            return displayedList.size();
        } else {
            return 0;
        }
    }

    public void setData(List<ContactDB> contactDBS) {
        this.contactDBS = contactDBS;
        this.displayedList = this.contactDBS;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = contactDBS;
                } else {
                    ArrayList<ContactDB> filteredList = new ArrayList<>();
                    for (ContactDB contactDB : contactDBS) {
                        if (contactDB.getName().toLowerCase().contains(charString)) {
                            filteredList.add(contactDB);
                        }
                    }
                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                DiffUtilCallBack diffUtilCallBack = new DiffUtilCallBack(displayedList, mFilteredList);
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallBack);
                adapter.displayedList = mFilteredList;
                diffResult.dispatchUpdatesTo(adapter);
            }
        };
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
            ContactDB contact = adapter.displayedList.get(posit);
            int ids = Integer.valueOf(contact.getIds());
            if (posit != RecyclerView.NO_POSITION) {
                onClickListener.onItemClick(ids);
            } else {
                Log.i(LOG_TAG, "Get position no position");
            }
        }
    }
}