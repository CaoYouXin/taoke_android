package com.github.caoyouxin.taoke.model;

public class ShareView {
    public String shortUrl;
    public String tPwd;

    @Override
    public String toString() {
        return shortUrl + '\n' + "复制这条消息，" + tPwd + "，打开【手机淘宝】即可查看";
    }
}
