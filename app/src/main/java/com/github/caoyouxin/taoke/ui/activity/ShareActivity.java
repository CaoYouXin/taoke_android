package com.github.caoyouxin.taoke.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
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
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
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

    @BindView(R.id.to_share)
    View toShare;

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

    private void shareImages() {
        showDynamicBoxCustomView(DYNAMIC_BOX_AV_PACMAN, ShareActivity.this);

        ShareHelper.ShareFrescoImageDownloader frescoImageDownloader = new ShareHelper.ShareFrescoImageDownloader();

        List<Observable<String>> observables = new ArrayList<>();

        for (com.github.caoyouxin.taoke.model.ShareImage shareImage : shareImageAdapter.getData()) {
            if (shareImage.selected) {
                Observable observable = Observable.create(subscriber -> {
                    frescoImageDownloader.download(this, shareImage.thumb, ShareHelper.configuration.getImageCachePath(this), new IImageDownloader.OnImageDownloadListener() {
                        @Override
                        public void onStart() {
                            System.out.println("start downloading");
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
                    });
                });

                observables.add(observable);
            }
        }

        Observable.zip(observables, objects -> {
            List<String> thumbs = new ArrayList<>();
            for (Object object : objects) {
                if (object instanceof String) {
                    thumbs.add((String) object);
                }
            }
            System.out.println(Arrays.toString(thumbs.toArray()));
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
        }).map(bitmap -> {
            Bitmap b = ShareActivity.this.getBitmapFromView(toShare);

            int width = bitmap.getWidth(), height = bitmap.getHeight();
            if (b.getWidth() > width) {
                width = b.getWidth();
                height = width * bitmap.getHeight() / bitmap.getWidth();
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
                    new Rect(0, width * origin.getHeight() / origin.getWidth(), width, height),
                    null);

            return bitmap;
        })
                .compose(RxHelper.rxSchedulerHelper())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(bitmap -> {
                    if (bitmap != null) {
                        ShareParamImage shareParamImage = new ShareParamImage(getTitle().toString(), "", "");
                        shareParamImage.setImage(new ShareImage(bitmap));
                        shareParamImage.setContent("分享图片");
                        ShareHelper.share(this, shareParamImage);
                    }
                    dismissDynamicBox(ShareActivity.this);
                }, throwable -> {
                    Timber.e(throwable);
                    dismissDynamicBox(ShareActivity.this);
                });
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
}
