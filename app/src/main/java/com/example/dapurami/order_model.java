package com.example.dapurami;

public class order_model {
    String id;
    String price_total;
    String method;
    String order_date;
    String status_order;

    public order_model(String id, String price_total, String method, String order_date, String status_order) {
        this.id = id;
        this.price_total = price_total;
        this.method = method;
        this.order_date = order_date;
        this.status_order = status_order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice_total() {
        return price_total;
    }

    public void setPrice_total(String price_total) {
        this.price_total = price_total;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getStatus_order() {
        return status_order;
    }

    public void setStatus_order(String status_order) {
        this.status_order = status_order;
    }
}
