package com.github.caoyouxin.taoke.model;

/**
 * Created by cls on 2017/11/13.
 */

public class UserResetPwdSubmit {

    public String phone;
    public String smsCode;
    public String pwd;

    public UserResetPwdSubmit(String phone, String smsCode, String pwd) {
        this.phone = phone;
        this.smsCode = smsCode;
        this.pwd = pwd;
    }
}
