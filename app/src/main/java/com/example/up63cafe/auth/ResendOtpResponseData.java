package com.example.up63cafe.auth;

public class ResendOtpResponseData {

    private boolean otpSend;

    public ResendOtpResponseData(boolean otpSend) {
        this.otpSend = otpSend;
    }

    public boolean isOtpSend() {
        return otpSend;
    }
}
