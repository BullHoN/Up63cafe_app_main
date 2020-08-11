package com.avit.up63cafe.notification;

public class NotificationReceiveData {

    private String orderId;
    private int status;
    private String message;

    public NotificationReceiveData(String orderId, int status, String message) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
    }

    public NotificationReceiveData(String orderId, int status) {
        this.orderId = orderId;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getStatus() {
        return status;
    }
}
