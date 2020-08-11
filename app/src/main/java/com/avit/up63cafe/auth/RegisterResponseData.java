package com.avit.up63cafe.auth;

public class RegisterResponseData {

    private boolean isAlreadyUser;

    private boolean isAccountVerified;

    public RegisterResponseData(boolean isAlreadyUser, boolean isAccountVerified) {
        this.isAlreadyUser = isAlreadyUser;
        this.isAccountVerified = isAccountVerified;
    }

    public boolean isAlreadyUser() {
        return isAlreadyUser;
    }

    public boolean isAccountVerified() {
        return isAccountVerified;
    }
}
