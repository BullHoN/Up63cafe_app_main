package com.example.up63cafe.ui.dashboard;

public class CheckoutDetailsReponseData {

    private boolean allowPayment;
    private int deliveryPrice;

    public CheckoutDetailsReponseData(boolean allowPayment, int deliveryPrice) {
        this.allowPayment = allowPayment;
        this.deliveryPrice = deliveryPrice;
    }

    public boolean isAllowPayment() {
        return allowPayment;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }
}
