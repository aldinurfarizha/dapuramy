package com.example.dapurami;

public class notification_model {
    String id_notification;
    String message;
    String tanggal;
    String id_order;

    public notification_model(String id_notification, String message, String tanggal, String id_order) {
        this.id_notification = id_notification;
        this.message = message;
        this.tanggal = tanggal;
        this.id_order = id_order;
    }

    public String getId_notification() {
        return id_notification;
    }

    public void setId_notification(String id_notification) {
        this.id_notification = id_notification;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }
}
