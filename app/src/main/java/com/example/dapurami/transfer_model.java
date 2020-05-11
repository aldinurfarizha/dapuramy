package com.example.dapurami;

public class transfer_model {
    String id_order;
    String total;
    String upload;

    public transfer_model(String id_order, String total, String upload) {
        this.id_order = id_order;
        this.total = total;
        this.upload = upload;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }
}
