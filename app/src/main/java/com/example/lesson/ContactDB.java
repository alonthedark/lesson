package com.example.lesson;

import com.orm.SugarRecord;

public class ContactDB extends SugarRecord<ContactDB> {

    private String name;
    private String phone;
    private String ids;
    private String email;


    public ContactDB() {
    }

    public ContactDB(String f_name, String phone, String ids, String email) {
        this.name = f_name;
        this.phone = phone;
        this.ids = ids;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getIds() {
        return ids;
    }

    public String getEmail() {
        return email;
    }


    @Override
    public String toString() {
        return "ContactDB{" +
                "name='" + name + '\'' +
                "phone='" + phone + '\'' +
                "ids='" + ids + '\'' +
                "email='" + email + '\'' +
                '}';
    }
}