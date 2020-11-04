package com.endavourhealth.legacy.models;

public class UserDetails {
    private String username;
    private String firstName;
    private String lastName;

    private String token;
    private String message;

    public String getUsername() {
        return username;
    }

    public UserDetails setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDetails setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDetails setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getToken() {
        return token;
    }

    public UserDetails setToken(String token) {
        this.token = token;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public UserDetails setMessage(String message) {
        this.message = message;
        return this;
    }
}
