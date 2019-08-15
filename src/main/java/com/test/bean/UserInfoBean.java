package com.test.bean;

public class UserInfoBean {
    private int id = 0;
    private String nickname = "";
    private String header = "";
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
