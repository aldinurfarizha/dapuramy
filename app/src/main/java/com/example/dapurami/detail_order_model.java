package com.example.dapurami;

public class detail_order_model {
    String id_product;
    String product_name;
    String price;
    String picture;
    String qty;

    public detail_order_model(String id_product, String product_name, String price, String picture, String qty) {
        this.id_product = id_product;
        this.product_name = product_name;
        this.price = price;
        this.picture = picture;
        this.qty = qty;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
