package com.example.smartindiahackathon;

public class UserModel {
    boolean selected;
    String name;
    String device_token;
    String email;
    String key;

    public UserModel(){

    }

    public UserModel(boolean selected, String name,String email,  String device_token) {
        this.selected = selected;
        this.name = name;
        this.email = email;
        this.device_token = device_token;
    }

    public boolean isSelectedTo() {
        return selected;
    }

    public void setSelectedTo(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
