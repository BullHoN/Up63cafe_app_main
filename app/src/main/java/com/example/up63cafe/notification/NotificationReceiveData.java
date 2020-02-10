package com.example.up63cafe.notification;

public class NotificationReceiveData {

    private String orderId;
    private int status;

    public NotificationReceiveData(String orderId, int status) {
        this.orderId = orderId;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getStatus() {
        return status;
    }
}
