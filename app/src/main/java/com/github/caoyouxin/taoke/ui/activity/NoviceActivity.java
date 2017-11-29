package com.github.caoyouxin.taoke.ui.activity;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.github.caoyouxin.taoke.model.UserData;
import com.github.caoyouxin.taoke.util.RatioImageView;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NoviceActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.novice_images_wrapper)
    LinearLayout noviceImagesWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_novice);

        ButterKnife.bind(this);

        title.setText(R.string.new_help);

        this.init();
    }

    private void init() {
        TaoKeApi.getNoviceImgList(UserData.get().isBuyer() ? 1 : 2)
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(
                        taoKeData -> {
                            for (String imageUri : taoKeData) {
                                final RatioImageView child = new RatioImageView(NoviceActivity.this);
                                child.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));
                                child.setAdjustViewBounds(true);
                                child.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                                        .setUri(imageUri)
                                        .setOldController(child.getController())
                                        .setControllerListener(new BaseControllerListener<ImageInfo>() {
                                            @Override
                                            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                                                child.setOriginalSizeAccordingToWidth(imageInfo.getWidth(), imageInfo.getHeight());
                                            }

                                            @Override
                                            public void onFailure(String id, Throwable throwable) {

                                            }
                                        }).build();
                                child.setController(draweeController);
                                noviceImagesWrapper.addView(child);
                            }
                        },
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

    @OnClick(R.id.back)
    protected void onBackClick(View view) {
        onBackPressed();
    }
}
