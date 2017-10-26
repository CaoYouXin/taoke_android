package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpReportActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help_report);

        ButterKnife.bind(this);

        title.setText(R.string.help_report);
    }

    @OnClick(R.id.back)
    protected void onBackClick(View view) {
        onBackPressed();
    }
}
