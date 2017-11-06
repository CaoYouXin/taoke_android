package com.github.caoyouxin.taoke.model;

/**
 * Created by cls on 2017/11/6.
 */

public class UserRegisterSubmit {
    public String code;
    public User user;

    public UserRegisterSubmit(String code, String phone, String pwd) {
        this.code = code;
        this.user = new User(phone, pwd);
    }

    private static class User {
        public String phone;
        public String pwd;

        public User(String phone, String pwd) {
            this.phone = phone;
            this.pwd = pwd;
        }
    }
}
