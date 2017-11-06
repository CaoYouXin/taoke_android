package com.github.caoyouxin.taoke.model;

/**
 * Created by cls on 2017/11/5.
 */

public class UserLoginSubmit {
    public String phone;
    public String pwd;

    public UserLoginSubmit(String phone, String pwd) {
        this.phone = phone;
        this.pwd = pwd;
    }
}
