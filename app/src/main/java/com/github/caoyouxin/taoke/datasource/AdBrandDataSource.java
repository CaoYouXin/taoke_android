package com.github.caoyouxin.taoke.datasource;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.AdBrandItem;
import com.shizhefei.mvc.IDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdBrandDataSource implements IDataSource<List<AdBrandItem>> {
    @Override
    public List<AdBrandItem> refresh() throws Exception {
        String thumb = "http://imglf4.nosdn.127.net/img/SGduRFlsUm9rb1lmR2UxM1FsUkFiRE5NWUw1eUxFWTM4N3RZSjh2dnM4MzBNclZYQk9BckVnPT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg";
        List<AdBrandItem> ret = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 24; i++) {
            AdBrandItem adBrandItem = new AdBrandItem();
            adBrandItem.rSpan = random.nextInt(3) + 1;
            adBrandItem.cSpan = random.nextInt(5) + 1;
            adBrandItem.thumb = thumb;
            ret.add(adBrandItem);
        }

        return ret;
    }

    @Override
    public List<AdBrandItem> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }
}
