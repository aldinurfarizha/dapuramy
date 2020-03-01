package com.example.dapurami;

public class User {

    private int id;
    private String name, phone_number;

    public User(int id, String name, String phone_number) {
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }

}
