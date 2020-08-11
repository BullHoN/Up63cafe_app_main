package com.avit.up63cafe.auth;

public class OtpPostData {

    private String name;
    private String email;
    private String otp;

    public OtpPostData(String name, String email,String otp) {
        this.name = name;
        this.email = email;
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
