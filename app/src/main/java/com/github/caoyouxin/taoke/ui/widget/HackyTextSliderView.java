package com.github.caoyouxin.taoke.ui.widget;

import android.content.Context;
import android.view.View;

import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.github.caoyouxin.taoke.R;


public class HackyTextSliderView extends TextSliderView {
    public HackyTextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View view = super.getView();
        //hack to hide description
        view.findViewById(R.id.description_layout).setBackgroundDrawable(null);
        return view;
    }
}
