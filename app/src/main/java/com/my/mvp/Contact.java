package com.my.mvp;

public class Contact {

    private String ids;
    private String name  = "";
    private String phone = "";
    private String email = "";

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIds() {
        return ids;
    }
    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
}
