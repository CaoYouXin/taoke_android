package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;

import com.github.caoyouxin.taoke.R;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

import butterknife.ButterKnife;

public class SelectorActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selector);

        ButterKnife.bind(this);
    }
}
