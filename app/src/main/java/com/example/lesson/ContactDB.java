package com.example.lesson;

import com.orm.SugarRecord;

public class ContactDB extends SugarRecord<ContactDB>{

    private String name;
    private String phone;
    private String id;


    public ContactDB() {
    }

    public ContactDB(String f_name, String phone, String id) {
        this.name = f_name;
        this.phone = phone;
        this.id = id;
    }

    public String getName() { return name; }
    public String getPhone(){ return phone; }
    public String getIds(){return id;}


    @Override
    public String toString() {
        return "ContactDB{" +
                "name='" + name + '\'' +
                "phone='" + phone +'\'' +
                "id='" + id +'\'' +
                '}';
    }
}