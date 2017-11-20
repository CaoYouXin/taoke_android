package com.github.caoyouxin.taoke.model;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.util.Date;


public class OrderItem implements Comparable<OrderItem> {
    public String itemName;
    public String itemStoreName;
    public String status;
    public String itemTradePrice;
    public String commission;
    public String dateStr;
    public String estimateEffect;
    public String estimateIncome;

    private Date date;

    public Date getDate() {
        if (null != this.date) {
            return this.date;
        }

        try {
            return this.date = M.DF.parse(this.dateStr);
        } catch (ParseException e) {
            return new Date();
        }
    }

    @Override
    public int compareTo(@NonNull OrderItem o) {
        return o.getDate().compareTo(this.getDate());
    }
}
