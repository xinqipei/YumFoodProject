package com.example.yummfoodapp.Model;

public class Details {
    String name;
    String email;

    public String getId() {
        return id;
    }

    String id;

    public Details(String id,String name, String email, String address, String number) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.number = number;
        this.id
                =id;
    }


    String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    String number;
}
