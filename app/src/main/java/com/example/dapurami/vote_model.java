package com.example.dapurami;

public class vote_model {
    String id_user;
    String id_product;

    public vote_model(String id_user, String id_product) {
        this.id_user = id_user;
        this.id_product = id_product;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }
}
