package com.github.caoyouxin.taoke.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;

import com.github.caoyouxin.taoke.R;

/**
 * Created by cls on 2017/10/29.
 */

public class SpannedTextUtil {

    public static SpannableStringBuilder buildAmount(final Context context, final int id, final String textV, final char start, final int offset) {
        String text = context.getResources().getString(id, textV);
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.font_28)), text.indexOf(start) + offset, text.indexOf('.'), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

}
