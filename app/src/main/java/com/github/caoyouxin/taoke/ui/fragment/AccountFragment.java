package com.github.caoyouxin.taoke.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.ui.activity.AboutActivity;
import com.github.caoyouxin.taoke.ui.activity.FriendsActivity;
import com.github.caoyouxin.taoke.ui.activity.HelpReportActivity;
import com.github.caoyouxin.taoke.ui.activity.NoviceActivity;
import com.github.caoyouxin.taoke.ui.activity.ShareActivity;
import com.github.caoyouxin.taoke.ui.activity.SplashActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.app_not_release_hint, Snackbar.LENGTH_LONG).show();
        }
        return rootView;
    }

    private void initAccountId() {
        SpannableString span = new SpannableString("账户ID:  954382491\n淘客954382491");
        span.setSpan(new RelativeSizeSpan(1.36f), 17, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        accountId.setText(span);
    }

    @OnClick({R.id.account_btn_about, R.id.account_btn_help_report, R.id.account_btn_newer_guide, R.id.account_btn_share_cmd, R.id.account_btn_friends, R.id.sign_out})
    protected void onToolClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.account_btn_newer_guide:
                intent = new Intent(getActivity(), NoviceActivity.class);
                break;
            case R.id.account_btn_help_report:
                intent = new Intent(getActivity(), HelpReportActivity.class);
                break;
            case R.id.account_btn_about:
                intent = new Intent(getActivity(), AboutActivity.class);
                break;
            case R.id.account_btn_friends:
                intent = new Intent(getActivity(), FriendsActivity.class);
                break;
            case R.id.account_btn_share_cmd:
                intent = new Intent(getActivity(), ShareActivity.class);
                break;
            case R.id.sign_out:
                new AlertDialog.Builder(getActivity()).setPositiveButton(R.string.sign_out, (DialogInterface dialog, int which) -> {
                    startActivity(new Intent(getActivity(), SplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    TaoKeApi.clearCustInfo();
                }).setNegativeButton(R.string.sign_out_cancel, (DialogInterface dialog, int which) -> dialog.dismiss()).setMessage(R.string.sign_out_confirm).show();
                return;
        }
        if (intent != null) {
            getActivity().startActivity(intent);
        }
    }

}
