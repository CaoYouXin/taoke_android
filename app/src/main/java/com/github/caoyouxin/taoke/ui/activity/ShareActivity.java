package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bilibili.socialize.share.core.SocializeMedia;
import com.bilibili.socialize.share.core.shareparam.ShareImage;
import com.bilibili.socialize.share.core.shareparam.ShareParamImage;
import com.bilibili.socialize.share.core.shareparam.ShareParamText;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.util.ShareHelper;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

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

        createDynamicBox();

        title.setText(R.string.share_title);
        handle.setText(R.string.share_submit);
    }

    @OnClick({R.id.back, R.id.handle, R.id.share_text_only})
    protected void onBackClick(View view) {
        if (R.id.back == view.getId()) {
            onBackPressed();
            return;
        }


        switch (view.getId()) {
            case R.id.handle:
                String thumbnail = "https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg";
                ShareParamImage shareParamImage = new ShareParamImage(getTitle().toString(), "", thumbnail);
                shareParamImage.setImage(new ShareImage(thumbnail));
                ShareHelper.share(this, shareParamImage, new ShareHelper.ShareListener() {

                    @Override
                    public void onStart(SocializeMedia type) {
                        super.onStart(type);
                    }

                    @Override
                    public void onProgress(SocializeMedia type, String progressDesc) {
                        super.onProgress(type, progressDesc);
                        showDynamicBoxCustomView(DYNAMIC_BOX_AV_PACMAN, ShareActivity.this);
                    }

                    @Override
                    public void onSuccess(SocializeMedia type, int code) {
                        super.onSuccess(type, code);
                        dismissDynamicBox(ShareActivity.this);
                    }

                    @Override
                    public void onError(SocializeMedia type, int code, Throwable error) {
                        super.onError(type, code, error);
                        dismissDynamicBox(ShareActivity.this);
                    }
                });

                break;
            case R.id.share_text_only:
                String text2Share = this.shareText.getText().toString();
                if (text2Share.isEmpty()) {
                    return;
                }
                ShareParamText shareParamText = new ShareParamText(getResources().getString(R.string.share_title), text2Share);
                ShareHelper.share(ShareActivity.this, shareParamText);
        }
    }
}
