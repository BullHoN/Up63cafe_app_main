package com.avit.up63cafe.ui.dashboard;

public class CheckoutDetailsReponseData {

    private boolean allowPayment;
    private int deliveryPrice;
    private String upiId;
    private String upiName;

    public CheckoutDetailsReponseData(boolean allowPayment, int deliveryPrice , String upiId,
                                      String upiName) {
        this.allowPayment = allowPayment;
        this.deliveryPrice = deliveryPrice;
        this.upiId = upiId;
        this.upiName = upiName;
    }

    public String getUpiId() {
        return upiId;
    }

    public String getUpiName() {
        return upiName;
    }

    public boolean isAllowPayment() {
        return allowPayment;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }
}
