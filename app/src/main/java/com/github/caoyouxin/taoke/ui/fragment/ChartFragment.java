package com.github.caoyouxin.taoke.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.api.ApiException;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.ui.activity.NoviceActivity;
import com.github.caoyouxin.taoke.ui.activity.OrdersActivity;
import com.github.caoyouxin.taoke.util.RatioImageView;
import com.github.caoyouxin.taoke.util.SpannedTextUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jasontsang on 10/24/17.
 */

public class ChartFragment extends Fragment {

    @BindView(R.id.user_amount)
    TextView userAmount;

    @BindView(R.id.user_this_month_amount)
    TextView userThisMonthAmount;

    @BindView(R.id.user_last_month_amount)
    TextView userLastMonthAmount;

    View rootView;

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

        }
        return rootView;
    }

    private void initUserAmount() {
        TaoKeApi.getUserAmount()
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(((RxAppCompatActivity) getActivity()).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(
                        taoKeData -> {
                            userAmount.setText(SpannedTextUtil.buildAmount(getActivity(), R.string.user_amount, taoKeData, '¥', 2));
                        },
                        throwable -> {
                            if (throwable instanceof TimeoutException) {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.fail_timeout, Snackbar.LENGTH_LONG).show();
                            } else if (throwable instanceof ApiException) {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.fail_message, throwable.getMessage()), Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.fail_network, Snackbar.LENGTH_LONG).show();
                            }
                        }
                );
    }

    private void initThisMonthEstimate() {
        TaoKeApi.getThisMonthEstimate()
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(((RxAppCompatActivity) getActivity()).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(
                        taoKeData -> {
                            userThisMonthAmount.setText(SpannedTextUtil.buildAmount(getActivity(), R.string.user_this_month_amount, taoKeData, '¥', 2));
                        },
                        throwable -> {
                            if (throwable instanceof TimeoutException) {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.fail_timeout, Snackbar.LENGTH_LONG).show();
                            } else if (throwable instanceof ApiException) {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.fail_message, throwable.getMessage()), Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.fail_network, Snackbar.LENGTH_LONG).show();
                            }
                        }
                );
    }

    private void initLastMonthEstimate() {
        TaoKeApi.getUserAmount()
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(((RxAppCompatActivity) getActivity()).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(
                        taoKeData -> {
                            userLastMonthAmount.setText(SpannedTextUtil.buildAmount(getActivity(), R.string.user_last_month_amount, taoKeData, '¥', 2));
                        },
                        throwable -> {
                            if (throwable instanceof TimeoutException) {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.fail_timeout, Snackbar.LENGTH_LONG).show();
                            } else if (throwable instanceof ApiException) {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.fail_message, throwable.getMessage()), Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.fail_network, Snackbar.LENGTH_LONG).show();
                            }
                        }
                );
    }

    @OnClick(R.id.orders_detail)
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), OrdersActivity.class);
        getActivity().startActivity(intent);
    }

}
