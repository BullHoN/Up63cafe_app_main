package com.example.up63cafe.payment;

public class PaymentOrderResponseData {

    private String order_id;
    private int amount;

    public PaymentOrderResponseData(String order_id, int amount) {
        this.order_id = order_id;
        this.amount = amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public int getAmount() {
        return amount/100;
    }
}
