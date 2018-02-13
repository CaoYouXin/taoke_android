package com.github.caoyouxin.taoke.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.ui.activity.CompleterActivity;
import com.github.caoyouxin.taoke.ui.activity.OrdersActivity;
import com.github.caoyouxin.taoke.util.SpannedTextUtil;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChartFragment extends Fragment {

    @BindView(R.id.user_amount)
    TextView userAmount;

    @BindView(R.id.user_this_month_amount)
    TextView userThisMonthAmount;

    @BindView(R.id.user_last_month_amount)
    TextView userLastMonthAmount;

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    View rootView;

    private Double userAmountNum;
    private boolean canDrawState = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_chart, container, false);
            ButterKnife.bind(this, rootView);

            if (savedInstanceState != null) {
                //restore
            }

            this.initUserAmount();
            this.initThisMonthEstimate();
            this.initLastMonthEstimate();

            this.initRefreshLayout();

        }
        return rootView;
    }

    private void initUserAmount() {
        TaoKeApi.getUserAmount()
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(((RxAppCompatActivity) getActivity()).bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.rxHandleServerExp(getActivity()))
                .subscribe(
                        taoKeData -> {
                            userAmount.setText(SpannedTextUtil.buildAmount(getActivity(), R.string.user_amount, taoKeData, '¥', 2));
                            userAmountNum = Double.parseDouble(taoKeData);
                            canDrawState = true;
                        }
                );
    }

    private void initThisMonthEstimate() {
        TaoKeApi.getThisMonthEstimate()
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(((RxAppCompatActivity) getActivity()).bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.rxHandleServerExp(getActivity()))
                .subscribe(
                        taoKeData -> {
                            userThisMonthAmount.setText(SpannedTextUtil.buildAmount(getActivity(), R.string.user_this_month_amount, taoKeData, '¥', 2));
                        }
                );
    }

    private void initLastMonthEstimate() {
        TaoKeApi.getLastMonthEstimate()
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(((RxAppCompatActivity) getActivity()).bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.rxHandleServerExp(getActivity()))
                .subscribe(
                        taoKeData -> {
                            userLastMonthAmount.setText(SpannedTextUtil.buildAmount(getActivity(), R.string.user_last_month_amount, taoKeData, '¥', 2));
                        }
                );
    }

    @OnClick({R.id.orders_detail, R.id.withdraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.orders_detail:
                Intent intent = new Intent(getActivity(), OrdersActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.withdraw:
                if (!this.canDrawState) {
                    return;
                }

                if (this.userAmountNum < 10.0) {
                    new AlertDialog.Builder(getActivity()).setMessage(R.string.user_amount_threshold).show();
                    return;
                }
                this.canDrawState = false;

                TaoKeApi.canWithdraw().compose(RxHelper.rxSchedulerHelper())
                        .compose(((BaseActivity) getActivity()).bindUntilEvent(ActivityEvent.DESTROY))
                        .compose(RxHelper.rxHandleServerExp(getActivity()))
                        .subscribe(can -> {
                            this.canDrawState = true;
                            if (can) {
                                showInput();
                            } else {
                                showCompleter();
                            }
                        });
                break;
        }
    }

    private void showCompleter() {
        FragmentActivity activity = getActivity();
        Intent intent = new Intent(activity, CompleterActivity.class);
        activity.startActivity(intent);
    }

    private void showInput() {
        EditText input = new EditText(getActivity());
        input.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(input)});
        input.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint(R.string.how_much_to_withdraw);

        new AlertDialog.Builder(getActivity()).setView(input)
                .setPositiveButton(R.string.withdraw, (DialogInterface dialog, int which) -> {
                    double withdraw = Double.parseDouble(input.getEditableText().toString().trim());
                    if (withdraw > userAmountNum) {
                        new AlertDialog.Builder(getActivity()).setMessage(R.string.user_amount_not_enough).show();
                        return;
                    }

                    TaoKeApi.sendWithdraw(String.format(Locale.ENGLISH, "%.2f", withdraw))
                            .timeout(10, TimeUnit.SECONDS)
                            .compose(RxHelper.rxSchedulerHelper())
                            .compose(((BaseActivity) getActivity()).bindUntilEvent(ActivityEvent.DESTROY))
                            .compose(RxHelper.rxHandleServerExp(getActivity()))
                            .subscribe(
                                    taoKeData -> {
                                        Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.withdraw_record_created, Snackbar.LENGTH_LONG).show();
                                        initUserAmount();
                                    }
                            );
                }).setNegativeButton(R.string.cancel, (DialogInterface dialog, int which) -> dialog.dismiss())
                .setMessage(R.string.withdraw_description).show();
    }

    private void initRefreshLayout() {
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            initUserAmount();
            initLastMonthEstimate();
            initThisMonthEstimate();
            refreshLayout.finishRefresh(2000);
        });
        smartRefreshLayout.setOnLoadmoreListener(refreshLayout -> {
            refreshLayout.finishLoadmore(false);
        });
    }

    private static class DecimalDigitsInputFilter implements InputFilter {

        private EditText context;

        public DecimalDigitsInputFilter(EditText context) {
            this.context = context;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String examStr = this.context.getText().toString() + source.toString();
            int indexOfDot = examStr.indexOf('.');
            if (-1 == indexOfDot) {
                try {
                    Integer.parseInt(examStr);
                    return null;
                } catch (Exception e) {
                    return "";
                }
            } else {
                String beforeDot = examStr.substring(0, indexOfDot);
                String afterDot = examStr.substring(indexOfDot + 1);

                if (afterDot.length() > 2) {
                    return "";
                }

                if (TextUtils.isEmpty(afterDot)) {
                    return null;
                }

                try {
                    Integer.parseInt(beforeDot);
                    Integer.parseInt(afterDot);
                    return null;
                } catch (Exception e) {
                    return "";
                }
            }
        }
    }

}
