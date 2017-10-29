package com.github.caoyouxin.taoke.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.handle)
    TextView handle;

    @BindView(R.id.share_text)
    EditText shareText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_share);

        ButterKnife.bind(this);

        title.setText(R.string.share_title);
        handle.setText(R.string.share_submit);
    }

    @OnClick({R.id.back, R.id.handle, R.id.share_text_only})
    protected void onBackClick(View view) {
        if (R.id.back == view.getId()) {
            onBackPressed();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        switch (view.getId()) {
            case R.id.handle:
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                try {
                    intent.putExtra(Intent.EXTRA_STREAM, new URL("http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg").toURI());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    return;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return;
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case R.id.share_text_only:
                String text2Share = this.shareText.getText().toString();
                if (text2Share.isEmpty()) {

                    return;
                }

                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(Intent.createChooser(intent, getTitle()));
    }
}
