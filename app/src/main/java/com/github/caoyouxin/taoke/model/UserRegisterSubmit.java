package com.github.caoyouxin.taoke.model;


public class UserRegisterSubmit {
    public String code;
    public User user;
    public String invitation;

    public UserRegisterSubmit(String code, String invitation, String phone, String pwd, String name) {
        this.code = code;
        this.invitation = invitation;
        this.user = new User(phone, pwd, name);
    }

    public UserRegisterSubmit(String code, String phone, String aliPay) {
        this.code = code;
        this.user = new User(phone, aliPay);
    }

    private static class User {
        public String aliPay;
        public String phone;
        public String pwd;
        public String name;

        public User(String phone, String pwd, String name) {
            this.phone = phone;
            this.pwd = pwd;
            this.name = name;
        }

        public User(String phone, String aliPay) {
            this.phone = phone;
            this.aliPay = aliPay;
        }
    }
}
