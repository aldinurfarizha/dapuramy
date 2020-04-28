package com.example.dapurami;

public class User {

    private int id;
    private String name, phone_number, address,status;

    public User(int id, String name, String phone_number, String address, String status) {
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
        this.address = address;
        this.status = status;

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

    public String getAddress() {
        return address;
    }
    public String getStatus() {
        return status;
    }



}
