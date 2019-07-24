package com.test.bean;

public class UserInfoBean {
    private String name;
    private int age;
    private int gender;
    private String header;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getGender() {
        return gender;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(int agender) {
        this.gender = agender;
    }

}
