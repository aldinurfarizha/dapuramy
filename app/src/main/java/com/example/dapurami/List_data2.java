package com.example.dapurami;


class List_data2 {
    private String id_product;
    private String product_name;
    private String description;
    private String stock;
    private String price;
    private String picture;

    public List_data2(String id_product, String product_name, String description, String stock, String price, String picture) {
        this.id_product = id_product;
        this.product_name = product_name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.picture = picture;
    }

    public String getId_product() {
        return id_product;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getDescription() {
        return description;
    }

    public String getStock() {
        return stock;
    }

    public String getPrice() {
        return price;
    }

    public String getPicture() {
        return picture;
    }
}

