package com.github.caoyouxin.taoke.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilibili.socialize.share.core.shareparam.ShareImage;
import com.bilibili.socialize.share.core.shareparam.ShareParamImage;
import com.bilibili.socialize.share.download.IImageDownloader;
import com.bilibili.socialize.share.util.BitmapUtil;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.ShareAppImageAdapter;
import com.github.caoyouxin.taoke.adapter.ShareImageAdapter;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.datasource.ShareAppImageDataSource;
import com.github.caoyouxin.taoke.datasource.ShareImageDataSource;
import com.github.caoyouxin.taoke.ui.widget.HackyLoadViewFactory;
import com.github.caoyouxin.taoke.util.ShareHelper;
import com.github.gnastnosaj.boilerplate.util.keyboard.BaseActivity;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCNormalHelper;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class ShareAppActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.handle)
    TextView handle;

    @BindView(R.id.share_image_list)
    RecyclerView shareImageList;

    @Nullable
    @BindView(R.id.view_stub)
    ViewStub viewStub;

    @Nullable
    @BindView(R.id.share_image_qr_desc)
    View shareImageQrDesc;

    @Nullable
    @BindView(R.id.desc_qrcode)
    ImageView descQrCode;

    @Nullable
    @BindView(R.id.share_code)
    TextView shareCode;

    private ShareAppImageAdapter shareImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_share_app);

        ButterKnife.bind(this);

        createDynamicBox();

        title.setText(R.string.share_title);
        handle.setText(R.string.share_submit);

        initShareImageList();

        initViewStub();
    }

    @OnClick({R.id.back, R.id.handle})
    protected void onBackClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.handle:
                shareImages();
                break;
        }
    }

    private void initShareImageList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        shareImageList.setLayoutManager(layoutManager);

        shareImageAdapter = new ShareAppImageAdapter(this);
        ShareAppImageDataSource shareImageDataSource = new ShareAppImageDataSource(this);

        HackyLoadViewFactory hackyLoadViewFactory = new HackyLoadViewFactory();
        MVCHelper<List<com.github.caoyouxin.taoke.model.ShareImage>> mvcHelper = new MVCNormalHelper<>(shareImageList, hackyLoadViewFactory.madeLoadView(), hackyLoadViewFactory.madeLoadMoreView());
        mvcHelper.setAdapter(shareImageAdapter);
        mvcHelper.setDataSource(shareImageDataSource);

        mvcHelper.refresh();
    }

    private void initViewStub() {
        if (shareImageQrDesc == null) {
            viewStub.inflate();
            ButterKnife.bind(this);

            String text = getResources().getString(R.string.share_code, TaoKeApi.shareCode);
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.orange_800));
            builder.setSpan(foregroundColorSpan, text.indexOf("->") + 2, text.indexOf("<-"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            shareCode.setText(builder);
        }
    }

    private void shareImages() {
        ShareHelper.ShareFrescoImageDownloader frescoImageDownloader = new ShareHelper.ShareFrescoImageDownloader();

        List<Observable<String>> observables = new ArrayList<>();

        for (com.github.caoyouxin.taoke.model.ShareImage shareImage : shareImageAdapter.getData()) {
            if (shareImage.selected) {
                Observable observable = Observable.create(subscriber ->
                        frescoImageDownloader.download(this, shareImage.thumb, ShareHelper.configuration.getImageCachePath(this), new IImageDownloader.OnImageDownloadListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(String s) {
                                subscriber.onNext(s);
                                subscriber.onComplete();
                            }

                            @Override
                            public void onFailed(String s) {
                                subscriber.onError(new Throwable(s));
                            }
                        })
                );

                observables.add(observable);
            }
        }

        if (observables.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.atleast_select_one, Snackbar.LENGTH_LONG).show();
            return;
        }

        showDynamicBoxCustomView(DYNAMIC_BOX_AV_PACMAN, ShareAppActivity.this);

        Observable.zip(observables, objects -> {
            List<String> thumbs = new ArrayList<>();
            for (Object object : objects) {
                if (object instanceof String) {
                    thumbs.add((String) object);
                }
            }
            return thumbs;
        }).map(thumbs -> {
            Bitmap bitmap = null;

            int width = 0, height = 0;

            for (String thumb : thumbs) {
                Bitmap b = BitmapFactory.decodeFile(thumb);
                if (b.getWidth() > width) {
                    height = width == 0 ? 0 : b.getWidth() * height / width;
                    width = b.getWidth();
                }
                height += width * b.getHeight() / b.getWidth();

                Bitmap origin = bitmap;
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                if (origin != null) {
                    canvas.drawBitmap(origin,
                            new Rect(0, 0, origin.getWidth(), origin.getHeight()),
                            new Rect(0, 0, width, width * origin.getHeight() / origin.getWidth()),
                            null);
                }
                canvas.drawBitmap(b,
                        new Rect(0, 0, b.getWidth(), b.getHeight()),
                        new Rect(0, height - width * b.getHeight() / b.getWidth(), width, height),
                        null);
            }

            return bitmap;
        }).zipWith(generateShareImageDescription(), (thumbs, description) -> {
            int thumbsWidth = thumbs.getWidth();
            int thumbsHeight = thumbs.getHeight();

            int descriptionWidth = description.getWidth();
            int descriptionHeight = description.getHeight();

            int width, height;
            if (thumbsWidth > descriptionWidth) {
                width = thumbsWidth;
                height = thumbsHeight + width * descriptionHeight / descriptionWidth;
            } else {
                width = descriptionWidth;
                height = descriptionHeight + width * thumbsHeight / thumbsWidth;
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(thumbs,
                    new Rect(0, 0, thumbs.getWidth(), thumbs.getHeight()),
                    new Rect(0, 0, width, width * thumbs.getHeight() / thumbs.getWidth()),
                    null);
            canvas.drawBitmap(description,
                    new Rect(0, 0, description.getWidth(), description.getHeight()),
                    new Rect(0, height - width * description.getHeight() / description.getWidth(), width, height),
                    null);

            return bitmap;
        }).map(bitmap -> BitmapUtil.saveBitmapToExternal(bitmap, new File(ShareHelper.configuration.getImageCachePath(this), UUID.randomUUID().toString().concat(".jpg"))))
                .compose(RxHelper.rxSchedulerHelper())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(shareImage -> {
                    if (shareImage != null) {
                        ShareParamImage shareParamImage = new ShareParamImage();
                        shareParamImage.setImage(new ShareImage(shareImage));
                        ShareHelper.share(this, shareParamImage);
                    }
                    dismissDynamicBox(ShareAppActivity.this);
                }, throwable -> {
                    Timber.e(throwable);
                    dismissDynamicBox(ShareAppActivity.this);
                });
    }

    private Observable<Bitmap> generateShareImageDescription() {
        return Observable.<Bitmap>create(subscriber -> {
            try {
//                descQrCode.setImageBitmap(QRCodeEncoder.syncEncodeQRCode(String.format("http://www.baidu.com?q=%d", TaoKeApi.userId), descQrCode.getWidth()));
                descQrCode.setImageBitmap(QRCodeEncoder.syncEncodeQRCode("https://www.baidu.com/s?tn=mswin_oem_dg&ie=utf-16&word=%E8%A7%85%E5%88%B8%E5%84%BF", descQrCode.getWidth()));
                Bitmap bitmap = Bitmap.createBitmap(shareImageQrDesc.getWidth(), shareImageQrDesc.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                shareImageQrDesc.draw(canvas);
                subscriber.onNext(bitmap);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }
}
