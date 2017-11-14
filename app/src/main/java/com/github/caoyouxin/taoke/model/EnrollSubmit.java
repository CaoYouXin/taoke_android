package com.github.caoyouxin.taoke.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.gnastnosaj.boilerplate.Boilerplate;

/**
 * Created by cls on 2017/11/14.
 */

public class EnrollSubmit {

    public static final String PREF_REALNAME = "PREF_REALNAME";
    public static final String PREF_ALIPAYID = "PREF_ALIPAYID";
    public static final String PREF_QQID = "PREF_QQID";
    public static final String PREF_WECHATID = "PREF_WECHATID";
    public static final String PREF_ANNOUNCEMENT = "PREF_ANNOUNCEMENT";

    public String realName;
    public String aliPayId;
    public String qqId;
    public String weChatId;
    public String announcement;

    public EnrollSubmit(String realName, String aliPayId, String qqId, String weChatId, String announcement) {
        this.realName = realName;
        this.aliPayId = aliPayId;
        this.qqId = qqId;
        this.weChatId = weChatId;
        this.announcement = announcement;
    }

    public void persist() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Boilerplate.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_REALNAME, this.realName);
        editor.putString(PREF_ALIPAYID, this.aliPayId);
        editor.putString(PREF_QQID, this.qqId);
        editor.putString(PREF_WECHATID, this.weChatId);
        editor.putString(PREF_ANNOUNCEMENT, this.announcement);
        editor.apply();
    }

    public static EnrollSubmit get() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Boilerplate.getInstance());
        String realName = sharedPreferences.getString(PREF_REALNAME, "");
        String aliPayId = sharedPreferences.getString(PREF_ALIPAYID, "");
        String qqId = sharedPreferences.getString(PREF_QQID, "");
        String weChatId = sharedPreferences.getString(PREF_WECHATID, "");
        String announcement = sharedPreferences.getString(PREF_ANNOUNCEMENT, "");
        return new EnrollSubmit(realName, aliPayId, qqId, weChatId, announcement);
    }

}
