package com.example.lesson;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class ReadContact {
    private static final String TAG = "Permission contact";

    private List<Contact> contacts = new ArrayList<>();
    private Cursor cursor;
    private Cursor pCur;
    private Cursor emailCu;
    private ContactListFragment contactListFragment;
    private int ids = 0;


    ReadContact(ContactListFragment contactListFragment) {
        this.contactListFragment = contactListFragment;
        ContactDB.deleteAll(ContactDB.class);
    }


    void readContacts(Context context) {
        Contact contact;

        try {
            cursor = context.getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            if ((cursor != null ? cursor.getCount() : 0) > 0) {
                while (cursor.moveToNext()) {
                    contact = new Contact();
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    String name = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    String hasPhone = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (Integer.parseInt(hasPhone) > 0) {
                        // extract phone number

                        contact.setIds(String.valueOf(ids));
                        ids++;
                        contact.setName(name);
                        pCur = context.getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);

                        emailCu = context.getContentResolver().query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{id}, null);


                        while (pCur.moveToNext()) {
                            String phone = pCur.getString(
                                    pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            contact.setPhone(phone);

                        }
                        while (emailCu.moveToNext()) {
                            String emailg = emailCu.getString(
                                    emailCu.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            if (emailg != null) {
                                contact.setEmail(emailg);
                            }
                        }
                        contacts.add(contact);
                    }
                    Log.d(TAG, "Contact id=" + contact.getIds() +
                            ", name=" + contact.getName() +
                            ", phone=" + contact.getPhone() +
                            ", email=" + contact.getEmail());
                    Log.d(TAG, contacts + "");
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (emailCu != null) {
                emailCu.close();
            }
            if (pCur != null) {
                pCur.close();
            }
        }

        for (int i = 0; i < contacts.size(); i++) {
            ContactDB contactDB = new ContactDB(
                    contacts.get(i).getName(),
                    contacts.get(i).getPhone(),
                    contacts.get(i).getIds(),
                    contacts.get(i).getEmail());
            contactDB.save();
        }
        contactListFragment.handler.sendEmptyMessage(ContactListFragment.CONTACT_READ);
    }

}

