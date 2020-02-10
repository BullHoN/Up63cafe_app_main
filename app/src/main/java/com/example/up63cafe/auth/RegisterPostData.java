package com.example.up63cafe.auth;

public class RegisterPostData {

    private String email;
    private String password;
    private String name;

    public RegisterPostData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
