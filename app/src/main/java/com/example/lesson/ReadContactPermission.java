package com.example.lesson;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ReadContactPermission {
    String TAG = "Permission contact";
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    Context context;
    public List<Contact> contacts = new ArrayList<>();
    Activity activity;
    Cursor cursor;
    Cursor pCur;
    Cursor emailCu;
    MainActivity ma;



    ReadContactPermission(Activity activity, Context context, MainActivity ma){
        this.context = context;
        this.activity = activity;
        this.ma = ma;
        ContactDB.deleteAll(ContactDB.class);
    }


    public void pemissionGranted() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
            // Разрешения чтения контактов имеются
            Log.d(TAG, "Permission is granted");
            readContacts(context);
        } else {
            // Разрешений нет
            Log.d(TAG, "Permission is not granted");

            // Запрос разрешений
            Log.d(TAG, "Request permissions");

            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission
                                    .READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    public void readContactPerission(){
        int permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            readContacts(context);
        }
        else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        }

    }


    public void readContacts(Context context)
    {
        Contact contact;
        cursor=context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                contact = new Contact();
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                contact.setIds(id);

                String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                contact.setName(name);

                String has_phone = cursor.getString(cursor.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (Integer.parseInt(has_phone) > 0) {
                    // extract phone number

                    pCur = context.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    emailCu = context.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);


                    while(pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact.setPhone(phone);

                    }
                    while(emailCu.moveToNext()){
                        String emailg = emailCu.getString(
                                emailCu.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                if (emailg != null) {
                                contact.setEmail(emailg);
                                }
                    }

                    contacts.add(contact);

                }
                Log.d(TAG, "Contact id="  + contact.getIds()    +
                        ", name="  + contact.getName () +
                        ", phone=" + contact.getPhone() +
                        ", email=" + contact.getEmail());
                Log.d(TAG, contacts+"");

            }

        }
        String s = contacts.get(0).getIds();
        for(int i = 0; i < contacts.size(); i++) {
            ContactDB contactDB = new ContactDB(
                    contacts.get(i).getName(),
                    contacts.get(i).getPhone(),
                    contacts.get(i).getIds(),
                    contacts.get(i).getEmail());
            contactDB.save();

        }
        ma.handler.sendEmptyMessage(0);
        cursor.close();
        emailCu.close();
        pCur.close();
    }

}

