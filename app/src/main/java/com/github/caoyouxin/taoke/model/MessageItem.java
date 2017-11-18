package com.github.caoyouxin.taoke.model;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by cls on 2017/10/27.
 */

public class MessageItem implements Comparable<MessageItem> {

    public String title;
    public String dateStr;
    public String content;

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
    public int compareTo(@NonNull MessageItem o) {
        return o.getDate().compareTo(this.getDate());
    }

}
