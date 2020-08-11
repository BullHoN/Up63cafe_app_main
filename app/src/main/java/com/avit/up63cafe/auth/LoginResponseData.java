package com.avit.up63cafe.auth;


public class LoginResponseData {

    private boolean isValidEmail;

    private boolean isPasswordCorrect;

    private boolean isAccountVerified;

    private String userName;

    public LoginResponseData(boolean isValidEmail, boolean isPasswordCorrect, boolean isAccountVerified,String userName) {
        this.isValidEmail = isValidEmail;
        this.isPasswordCorrect = isPasswordCorrect;
        this.isAccountVerified = isAccountVerified;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isValidEmail() {
        return isValidEmail;
    }

    public boolean isPasswordCorrect() {
        return isPasswordCorrect;
    }

    public boolean isAccountVerified() {
        return isAccountVerified;
    }
}
