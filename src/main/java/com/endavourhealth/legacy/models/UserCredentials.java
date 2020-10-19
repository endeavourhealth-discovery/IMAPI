package com.endavourhealth.legacy.models;

public class UserCredentials {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public UserCredentials setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserCredentials setPassword(String password) {
        this.password = password;
        return this;
    }
}
