package com.github.caoyouxin.taoke.api;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by jasontsang on 10/24/17.
 */

public class TaoKeData {
    public Map<String, Object> header;
    public Map<String, Object> body;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
