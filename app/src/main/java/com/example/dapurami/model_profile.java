package com.example.dapurami;

public class model_profile {
    String id_customer;
    String name;
    String address;
    String phone_number;

    public model_profile(String id_customer, String name, String address, String phone_number) {
        this.id_customer = id_customer;
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
    }

    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
