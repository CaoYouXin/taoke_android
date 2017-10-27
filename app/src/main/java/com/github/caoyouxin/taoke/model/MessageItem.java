package com.github.caoyouxin.taoke.model;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cls on 2017/10/27.
 */

public class MessageItem implements Comparable<MessageItem> {
    public static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat SDF = new SimpleDateFormat("MM月dd日");

    public String title;
    public String dateStr;
    public String content;

    private Date date;

    public Date getDate() {
        if (null != this.date) {
            return this.date;
        }

        try {
            return this.date = DF.parse(this.dateStr);
        } catch (ParseException e) {
            return new Date();
        }
    }

    @Override
    public int compareTo(@NonNull MessageItem o) {
        return this.getDate().compareTo(o.getDate());
    }
}
