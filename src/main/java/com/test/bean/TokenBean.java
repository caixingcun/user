package com.test.bean;

public class TokenBean  {
    private String account;
    private String token;

    public TokenBean() {
    }

    public TokenBean(String account, String token) {
        this.account = account;
        this.token = token;
    }

    public String getAccount() {
        return account;
    }

    public String getToken() {
        return token;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
