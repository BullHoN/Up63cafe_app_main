package com.example.up63cafe.payment;

public class PaymentOrderPostData {

    private String email;
    private String name;
    private int amount;

    public PaymentOrderPostData(String email, String name, int amount) {
        this.email = email;
        this.name = name;
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
