package com.example.dapurami;

public class cart_model {
    private int id_order;
    private String id_product;
    private  String product_name;
    private String qty;
    private String img;

    public cart_model(int id_order, String id_product, String product_name, String qty, String img) {
        this.id_order = id_order;
        this.id_product = id_product;
        this.product_name = product_name;
        this.qty = qty;
        this.img=img;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
