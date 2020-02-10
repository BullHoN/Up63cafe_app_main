package com.example.up63cafe.notification;

public class NotificationPostData {

    private String name;
    private String email;
    private String phNumber;
    private String fcm_id;
    private int total;
    private String orderItems;
    private String address;
    private String nearBy;
    private String orderId;

    public NotificationPostData(String name, String email, String phNumber
            , String fcm_id, int total, String orderItems
            ,String address,String nearBy , String orderId) {
        this.name = name;
        this.email = email;
        this.phNumber = phNumber;
        this.fcm_id = fcm_id;
        this.total = total;
        this.orderItems = orderItems;
        this.address = address;
        this.nearBy = nearBy;
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public String getFcm_id() {
        return fcm_id;
    }

    public int getTotal() {
        return total;
    }

    public String getOrderItems() {
        return orderItems;
    }
}
