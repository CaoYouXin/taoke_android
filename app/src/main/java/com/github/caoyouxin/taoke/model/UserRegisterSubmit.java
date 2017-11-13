package com.github.caoyouxin.taoke.model;

/**
 * Created by cls on 2017/11/6.
 */

public class UserRegisterSubmit {
    public String code;
    public User user;

    public UserRegisterSubmit(String code, String phone, String pwd, String name) {
        this.code = code;
        this.user = new User(phone, pwd, name);
    }

    private static class User {
        public String phone;
        public String pwd;
        public String name;

        public User(String phone, String pwd, String name) {
            this.phone = phone;
            this.pwd = pwd;
            this.name = name;
        }
    }
}
