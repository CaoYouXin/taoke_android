package com.github.caoyouxin.taoke.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.caoyouxin.taoke.api.TaoKeData;
import com.github.gnastnosaj.boilerplate.Boilerplate;

import java.util.Map;


public class UserData {

    private final static String PREF_ACCESS_TOKEN = "token";
    private final static String PREF_USER = "user";
    private final static String PREF_USER_NAME = "name";
    private final static String PREF_USER_PID = "aliPid";
    private final static String PREF_USER_ID = "id";
    private final static String PREF_USER_SHARE_CODE = "code";
    private final static String PREF_CANDIDATE = "candidate";
    private final static String PREF_DIRECT_USER = "directUser";

    private static UserData INSTANCE;

    private String userName;
    private String aliPID;
    private Long userId;
    private String shareCode;
    private Boolean candidate;
    private String accessToken;
    public Boolean directUser;

    private UserData() {
    }

    public static UserData set(TaoKeData taoKeData) {
        INSTANCE = new UserData();
        Map rec = taoKeData.getMap();
        INSTANCE.accessToken = (String) rec.get(PREF_ACCESS_TOKEN);
        INSTANCE.candidate = (Boolean) rec.get(PREF_CANDIDATE);
        INSTANCE.directUser = (Boolean) rec.get(PREF_DIRECT_USER);
        Map user = (Map) rec.get(PREF_USER);
        INSTANCE.userName = (String) user.get(PREF_USER_NAME);
        INSTANCE.aliPID = (String) user.get(PREF_USER_PID);
        INSTANCE.userId = ((Double) user.get(PREF_USER_ID)).longValue();
        INSTANCE.shareCode = (String) user.get(PREF_USER_SHARE_CODE);
        return INSTANCE;
    }

    public static boolean restore() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Boilerplate.getInstance());
        if (sharedPreferences.contains(PREF_ACCESS_TOKEN)) {
            INSTANCE = new UserData();
            INSTANCE.accessToken = sharedPreferences.getString(PREF_ACCESS_TOKEN, null);
            INSTANCE.userName = sharedPreferences.getString(PREF_USER_NAME, null);
            INSTANCE.aliPID = sharedPreferences.getString(PREF_USER_PID, "");
            INSTANCE.userId = sharedPreferences.getLong(PREF_USER_ID, 0L);
            INSTANCE.shareCode = sharedPreferences.getString(PREF_USER_SHARE_CODE, "");
            INSTANCE.candidate = sharedPreferences.getBoolean(PREF_CANDIDATE, false);
            INSTANCE.directUser = sharedPreferences.getBoolean(PREF_DIRECT_USER, false);
            return true;
        } else {
            return false;
        }
    }

    public static void clear() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Boilerplate.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PREF_ACCESS_TOKEN);
        editor.remove(PREF_USER_NAME);
        editor.remove(PREF_USER_PID);
        editor.remove(PREF_USER_ID);
        editor.remove(PREF_USER_SHARE_CODE);
        editor.remove(PREF_CANDIDATE);
        editor.remove(PREF_DIRECT_USER);
        editor.apply();
        INSTANCE = null;
    }

    public static UserData get() {
        return INSTANCE;
    }

    public void cache() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Boilerplate.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_ACCESS_TOKEN, accessToken);
        editor.putString(PREF_USER_NAME, userName);
        editor.putString(PREF_USER_PID, aliPID);
        editor.putLong(PREF_USER_ID, userId);
        editor.putString(PREF_USER_SHARE_CODE, shareCode);
        editor.putBoolean(PREF_CANDIDATE, candidate);
        editor.putBoolean(PREF_DIRECT_USER, directUser);
        editor.apply();
    }

    public boolean isBuyer() {
        return null == shareCode || shareCode.isEmpty();
    }

    public String getUserType() {
        if (isBuyer()) {
            return "\"消费者\"";
        }

        if (directUser) {
            return "\"平台合伙人\"";
        }

        return "\"合伙人\"";
    }

    public String getAliPID() {
        return aliPID;
    }

    public String getShareCode() {
        return shareCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getShareAppType() {
        if (isBuyer()) {
            return 2;
        }

        if (directUser) {
            return 1;
        }

        return 2;
    }

    public Boolean getCandidate() {
        return candidate;
    }

    public String getUserName() {
        return userName;
    }
}
