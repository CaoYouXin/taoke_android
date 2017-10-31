package com.github.caoyouxin.taoke.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

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

        initShareImageList();

        initViewStub();
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

            descTitle.setText(couponItem.title);

            String text = getResources().getString(R.string.share_price_before, couponItem.priceBefore);
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
            builder.setSpan(strikethroughSpan, text.indexOf("¥") + 2, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            descPriceBefore.setText(builder);

            descCoupon.setText(getResources().getString(R.string.share_coupon_value, couponItem.value));

            text = getResources().getString(R.string.share_price_after, couponItem.priceAfter);
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

            int width = 0;
            int height = 0;

            for (String thumb : thumbs) {
                Bitmap b = BitmapFactory.decodeFile(thumb);
                if (b.getWidth() > width) {
                    width = b.getWidth();
                }
                height += b.getHeight();

                Bitmap origin = bitmap;
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                if (origin != null) {
                    canvas.drawBitmap(origin, 0, 0, null);
                }
                canvas.drawBitmap(b, 0, height - b.getHeight(), null);
            }

            return bitmap;
        }).zipWith(generateShareImageDescription(), (thumbs, description) -> {
            int thumbsWidth = thumbs.getWidth();
            int thumbsHeight = thumbs.getHeight();

            int descriptionWidth = description.getWidth();
            int descriptionHeight = description.getHeight();

            Bitmap bitmap = Bitmap.createBitmap(Math.max(descriptionWidth, thumbsWidth), thumbsHeight + descriptionHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(thumbs, 0, 0, null);
            canvas.drawBitmap(description, 0, thumbsHeight, null);

            return bitmap;
        })
                .compose(RxHelper.rxSchedulerHelper())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(bitmap -> {
                    if (bitmap != null) {
                        ShareParamImage shareParamImage = new ShareParamImage(getTitle().toString(), "", "");
                        shareParamImage.setImage(new ShareImage(bitmap));
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
                descQRcode.setImageBitmap(QRCodeEncoder.syncEncodeQRCode(couponItem.thumb, descQRcode.getWidth()));
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
