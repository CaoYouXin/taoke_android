package com.github.caoyouxin.taoke.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.api.ApiException;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.handle)
    TextView handle;

    @BindView(R.id.report_text)
    EditText reportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report);

        ButterKnife.bind(this);

        title.setText(R.string.feedback);
        handle.setText(R.string.submit);
    }

    @OnClick({R.id.back, R.id.handle})
    protected void onBackClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.handle:
                onSubmitReport();
                break;
        }
    }

    private void onSubmitReport() {
        String reportContent = reportText.getText().toString().trim();
        if (TextUtils.isEmpty(reportContent)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.need_report_content, Snackbar.LENGTH_LONG).show();
            reportText.requestFocus();
            return;
        }

        TaoKeApi.sendReport(reportContent)
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.rxHandleServerExp(this))
                .subscribe(
                        taoKeData -> new AlertDialog.Builder(this)
                                .setPositiveButton(R.string.get_it, (dialog, which) -> onBackPressed())
                                .setMessage(R.string.get_report).show(),
                        throwable -> {
                            if (throwable instanceof TimeoutException) {
                                Snackbar.make(findViewById(android.R.id.content), R.string.fail_timeout, Snackbar.LENGTH_LONG).show();
                            } else if (throwable instanceof ApiException) {
                                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.fail_message, throwable.getMessage()), Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(findViewById(android.R.id.content), R.string.fail_network, Snackbar.LENGTH_LONG).show();
                            }
                        }
                );
    }
}
