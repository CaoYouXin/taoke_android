package com.github.caoyouxin.taoke.model;

import java.util.List;

public class ShareSubmit2 {

    private List<String> images;
    private String title;
    private String url;

    public ShareSubmit2(List<String> images, String title, String url) {
        this.images = images;
        this.title = title;
        this.url = url;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
