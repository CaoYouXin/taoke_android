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
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilibili.socialize.share.core.shareparam.ShareImage;
import com.bilibili.socialize.share.core.shareparam.ShareParamImage;
import com.bilibili.socialize.share.core.shareparam.ShareParamText;
import com.bilibili.socialize.share.download.IImageDownloader;
import com.bilibili.socialize.share.util.BitmapUtil;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.ShareImageAdapter;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.datasource.ShareImageDataSource;
import com.github.caoyouxin.taoke.model.CouponItem;
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

public class ShareActivity extends BaseActivity {
    public final static String EXTRA_COUPON_ITEM = "couponItem";

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.handle)
    TextView handle;

    @BindView(R.id.share_image_selected)
    TextView shareImageSelected;

    @BindView(R.id.share_image_list)
    RecyclerView shareImageList;

    @BindView(R.id.share_text)
    EditText shareText;

    @Nullable
    @BindView(R.id.view_stub)
    ViewStub viewStub;

    @Nullable
    @BindView(R.id.share_image_description)
    View shareImageDescription;

    @Nullable
    @BindView(R.id.desc_title)
    TextView descTitle;

    @Nullable
    @BindView(R.id.desc_price_before)
    TextView descPriceBefore;

    @Nullable
    @BindView(R.id.desc_coupon)
    TextView descCoupon;

    @Nullable
    @BindView(R.id.desc_price_after)
    TextView descPriceAfter;

    @Nullable
    @BindView(R.id.desc_qrcode)
    ImageView descQRcode;

    private CouponItem couponItem;

    private ShareImageAdapter shareImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_share);

        ButterKnife.bind(this);

        couponItem = getIntent().getParcelableExtra(EXTRA_COUPON_ITEM);

        createDynamicBox();

        title.setText(R.string.share_title);
        handle.setText(R.string.share_submit);
        shareText.setText(getResources().getString(R.string.share_text, couponItem.getTitle(), couponItem.getZkFinalPrice(), couponItem.getCouponPrice()));

        initShareImageList();

        initViewStub();

        Snackbar.make(findViewById(android.R.id.content), R.string.app_not_release_hint, Snackbar.LENGTH_LONG).show();
    }

    @OnClick({R.id.back, R.id.handle, R.id.share_text_only})
    protected void onBackClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.handle:
                shareImages();
                break;
            case R.id.share_text_only:
                String text2Share = shareText.getText().toString().trim();
                if (!text2Share.isEmpty()) {
//                    String linkHint = getResources().getString(R.string.share_text_link_hint);
//                    String link = getResources().getString(R.string.share_text_link, couponItem.thumb, String.valueOf(couponItem.thumb.hashCode()));
//                    if (text2Share.contains(linkHint)) {
//                        text2Share = text2Share.replace(linkHint, link);
//                    } else {
//                        text2Share += link;
//                    }
                    ShareParamText shareParamText = new ShareParamText(getResources().getString(R.string.share_title), text2Share);
                    ShareHelper.share(ShareActivity.this, shareParamText);
                }
                break;
        }
    }

    @Override
    public int[] hideSoftByEditViewIds() {
        int[] ids = {R.id.share_text};
        return ids;
    }

    private void initShareImageList() {
        ShareImageAdapter.ShareImageEvent.observable
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(shareImageEvent -> updateShareImageSelected());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        shareImageList.setLayoutManager(layoutManager);

        shareImageAdapter = new ShareImageAdapter(this);
        ShareImageDataSource shareImageDataSource = new ShareImageDataSource(this, couponItem);

        //hacky to remove mvchelper loadview loadmoreview
        HackyLoadViewFactory hackyLoadViewFactory = new HackyLoadViewFactory();
        MVCHelper mvcHelper = new MVCNormalHelper(shareImageList, hackyLoadViewFactory.madeLoadView(), hackyLoadViewFactory.madeLoadMoreView());
        mvcHelper.setAdapter(shareImageAdapter);
        mvcHelper.setDataSource(shareImageDataSource);

        mvcHelper.refresh();
    }

    private void updateShareImageSelected() {
        int count = 0;
        for (com.github.caoyouxin.taoke.model.ShareImage shareImage : shareImageAdapter.getData()) {
            if (shareImage.selected) {
                count++;
            }
        }
        String selected = String.valueOf(count);
        String text = getResources().getString(R.string.selected_images, selected);
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.orange_900));
        builder.setSpan(foregroundColorSpan, text.indexOf(selected), text.indexOf(selected) + selected.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        shareImageSelected.setText(builder);
    }

    private void initViewStub() {
        if (shareImageDescription == null) {
            viewStub.inflate();
            ButterKnife.bind(this);

            descTitle.setText(couponItem.getTitle());

            String text = getResources().getString(R.string.share_price_before, couponItem.getZkFinalPrice());
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
            builder.setSpan(strikethroughSpan, text.indexOf("¥") + 2, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            descPriceBefore.setText(builder);

            descCoupon.setText(getResources().getString(R.string.share_coupon_value, couponItem.getCouponInfo()));

            text = getResources().getString(R.string.share_price_after, couponItem.getCouponPrice());
            builder = new SpannableStringBuilder(text);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.orange_800));
            builder.setSpan(foregroundColorSpan, text.indexOf("¥"), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.font_18)), text.indexOf("¥") + 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            descPriceAfter.setText(builder);
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
            return;
        }

        showDynamicBoxCustomView(DYNAMIC_BOX_AV_PACMAN, ShareActivity.this);

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
                    dismissDynamicBox(ShareActivity.this);
                }, throwable -> {
                    Timber.e(throwable);
                    dismissDynamicBox(ShareActivity.this);
                });
    }

    private Observable<Bitmap> generateShareImageDescription() {
        return Observable.<Bitmap>create(subscriber -> {
            try {
                descQRcode.setImageBitmap(QRCodeEncoder.syncEncodeQRCode(couponItem.getCouponClickUrl(), descQRcode.getWidth()));
                Bitmap bitmap = Bitmap.createBitmap(shareImageDescription.getWidth(), shareImageDescription.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                shareImageDescription.draw(canvas);
                subscriber.onNext(bitmap);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }
}
