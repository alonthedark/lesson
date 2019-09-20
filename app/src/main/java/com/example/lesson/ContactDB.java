package com.example.lesson;

import com.orm.SugarRecord;

public class ContactDB extends SugarRecord<ContactDB>{

    private String name;
    private String phone;
    private String id;
    private String email;



    public ContactDB() {
    }

    public ContactDB(String f_name, String phone, String id, String email) {
        this.name = f_name;
        this.phone = phone;
        this.id = id;
        this.email = email;
    }

    public String getName() { return name; }
    public String getPhone(){ return phone; }
    public String getIds(){return id;}
    public String getEmail(){return email;}


    @Override
    public String toString() {
        return "ContactDB{" +
                "name='" + name + '\'' +
                "phone='" + phone +'\'' +
                "id='" + id +'\'' +
                "email='" + email +'\'' +
                '}';
    }
}