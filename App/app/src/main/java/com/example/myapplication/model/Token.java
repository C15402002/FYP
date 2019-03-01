package com.example.myapplication.model;

public class Token {
    public boolean serverToken;
    public String token;

    public Token() {
    }

    public Token(boolean serverToken, String token) {
        this.serverToken = serverToken;
        this.token = token;
    }

    public boolean isServerToken() {
        return serverToken;
    }

    public void setServerToken(boolean serverToken) {
        this.serverToken = serverToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
