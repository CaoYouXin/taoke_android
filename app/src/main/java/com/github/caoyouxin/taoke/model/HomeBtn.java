package com.github.caoyouxin.taoke.model;


import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

public class HomeBtn extends RealmObject {

    public String imgUrl;
    public String name;
    public Integer openType;
    public String ext;

    public static List<HomeBtn> from(List<HomeBtn> results) {
        List<HomeBtn> data = new ArrayList<>();
        for (HomeBtn homeBtn : results) {
            data.add(homeBtn.clone());
        }
        return data;
    }

    @Override
    protected HomeBtn clone() {
        HomeBtn homeBtn = new HomeBtn();
        homeBtn.imgUrl = imgUrl;
        homeBtn.name = name;
        homeBtn.openType = openType;
        homeBtn.ext = ext;
        return homeBtn;
    }
}
