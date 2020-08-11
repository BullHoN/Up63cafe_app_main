package com.avit.up63cafe.auth;

public class OptResponseData {

    private boolean isValidOtp;

    public OptResponseData(boolean isValidOtp) {
        this.isValidOtp = isValidOtp;
    }

    public boolean isValidOtp() {
        return isValidOtp;
    }
}
