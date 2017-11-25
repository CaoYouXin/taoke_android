package com.github.caoyouxin.taoke.api;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;


public class TaoKeData {
    Integer code;
    Object body;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public List<Map> getList() {
        return (List<Map>) body;
    }

    public Map getMap() {
        return (Map) body;
    }

    public List<String> getStringList() {
        return (List<String>) body;
    }

    public Long getLong() {
        return ((Double) body).longValue();
    }
}
