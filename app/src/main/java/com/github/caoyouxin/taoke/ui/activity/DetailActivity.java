package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jasontsang on 10/26/17.
 */

public class DetailActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        title.setText(R.string.detail);
    }

    @OnClick(R.id.back)
    protected void onBackClick(View view) {
        onBackPressed();
    }
}
