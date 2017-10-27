package com.github.caoyouxin.taoke.ui.fragment;

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

            this.userAmount.setText(this.buildAmount(R.string.user_amount, "0.00"));
            this.userThisMonthAmount.setText(this.buildAmount(R.string.user_this_month_amount, "0.00"));
            this.userLastMonthAmount.setText(this.buildAmount(R.string.user_last_month_amount, "120.00"));
        }
        return rootView;
    }

    private SpannableStringBuilder buildAmount(final int id, final String amount) {
        String text = getActivity().getResources().getString(id, amount);
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new AbsoluteSizeSpan(getActivity().getResources().getDimensionPixelSize(R.dimen.font_28)), text.indexOf('¥') + 2, text.indexOf('.'), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    @OnClick(R.id.orders_detail)
    public void onClick(View view) {

    }

}
