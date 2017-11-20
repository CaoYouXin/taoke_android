package com.github.caoyouxin.taoke.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.github.gnastnosaj.boilerplate.Boilerplate;

import java.util.ArrayList;
import java.util.List;


public class SearchHistory {

    private static final String PREF_KEYWORDS = "PREF_KEYWORDS";
    private List<String> history;

    public List<String> get() {
        if (null == this.history) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Boilerplate.getInstance());
            String string = sharedPreferences.getString(PREF_KEYWORDS, "");
            this.history = new ArrayList<>();
            for (String keyword : string.split("\\|\\|")) {
                if (TextUtils.isEmpty(keyword)) {
                    continue;
                }

                this.history.add(keyword);
            }
        }

        return this.history;
    }

    public void add(String keyword) {
        boolean found = false;
        for (String word : this.history) {
            if (word.equals(keyword)) {
                found = true;
                break;
            }
        }

        if (found) {
            return;
        }
        this.history.add(0, keyword);
    }

    public void clear() {
        this.history = new ArrayList<>();
    }

    public void persist() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Boilerplate.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (null == this.history || this.history.isEmpty()) {
            editor.putString(PREF_KEYWORDS, "");
            editor.apply();
            return;
        }

        StringBuilder sb = new StringBuilder(this.history.get(0));
        for (String keyword : this.history.subList(1, this.history.size())) {
            if (sb.length() + 2 + keyword.length() > 255) {
                continue;
            }

            sb.append("||").append(keyword);
        }
        editor.putString(PREF_KEYWORDS, sb.toString());
        editor.apply();
    }

}
