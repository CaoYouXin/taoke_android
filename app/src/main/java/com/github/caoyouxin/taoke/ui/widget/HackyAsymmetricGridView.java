package com.github.caoyouxin.taoke.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;

public class HackyAsymmetricGridView extends AsymmetricGridView {
    public HackyAsymmetricGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
