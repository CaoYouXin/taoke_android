package com.github.caoyouxin.taoke.ui.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jasontsang on 10/24/17.
 */

public class AccountFragment extends Fragment {

    @BindView(R.id.account_id)
    TextView accountId;

    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_account, container, false);
            ButterKnife.bind(this, rootView);

            if (savedInstanceState != null) {
                //restore
            }

            this.initAccountId();
        }
        return rootView;
    }

    private void initAccountId() {
        SpannableString span = new SpannableString("账户ID:  954382491\n淘客954382491");
        span.setSpan(new RelativeSizeSpan(1.36f), 17, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        accountId.setText(span);
    }
}
