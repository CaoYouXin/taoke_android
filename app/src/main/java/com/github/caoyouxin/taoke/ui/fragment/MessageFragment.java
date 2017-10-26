package com.github.caoyouxin.taoke.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jasontsang on 10/24/17.
 */

public class MessageFragment extends Fragment {

    @BindView(R.id.global_msg)
    TextView globalMsg;

    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_message, container, false);
            ButterKnife.bind(this, rootView);

            if (savedInstanceState != null) {
                //restore
            }

            this.initAccountId();
        }
        return rootView;
    }

    private void initAccountId() {
        SpannableString span = new SpannableString("动态\n淘宝客新模式");
        span.setSpan(new RelativeSizeSpan(1.36f), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.DKGRAY), 3, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        globalMsg.setText(span);
    }
}
