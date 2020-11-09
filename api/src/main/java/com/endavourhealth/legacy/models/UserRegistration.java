package com.endavourhealth.legacy.models;

public class UserRegistration extends UserDetails {
    private String password;

    public String getPassword() {
        return password;
    }

    public UserRegistration setPassword(String password) {
        this.password = password;
        return this;
    }
}
