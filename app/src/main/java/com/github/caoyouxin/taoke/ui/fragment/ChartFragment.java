package com.github.caoyouxin.taoke.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.ui.activity.OrdersActivity;
import com.github.caoyouxin.taoke.util.SpannedTextUtil;

import org.w3c.dom.Text;

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

            this.userAmount.setText(SpannedTextUtil.buildAmount(getActivity(), R.string.user_amount, "0.00", '¥', 2));
            this.userThisMonthAmount.setText(SpannedTextUtil.buildAmount(getActivity(), R.string.user_this_month_amount, "0.00", '¥', 2));
            this.userLastMonthAmount.setText(SpannedTextUtil.buildAmount(getActivity(), R.string.user_last_month_amount, "120.00", '¥', 2));
        }
        return rootView;
    }

    @OnClick(R.id.orders_detail)
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), OrdersActivity.class);
        getActivity().startActivity(intent);
    }

}
