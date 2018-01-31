package com.github.caoyouxin.taoke.model;

import java.util.List;

public class FavItemsView {

    private List<CouponItem> items;
    private List<Long> orders;

    public FavItemsView(List<CouponItem> uatmTbkItems, List<Long> favOrder) {
        this.items = uatmTbkItems;
        this.orders = favOrder;
    }

    public List<CouponItem> getItems() {
        return items;
    }

    public void setItems(List<CouponItem> items) {
        this.items = items;
    }

    public List<Long> getOrders() {
        return orders;
    }

    public void setOrders(List<Long> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "FavItemsView{" +
                "items=" + items +
                ", orders=" + orders +
                '}';
    }
}
