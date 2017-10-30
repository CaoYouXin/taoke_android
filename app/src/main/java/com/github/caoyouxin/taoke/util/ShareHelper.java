package com.github.caoyouxin.taoke.util;

import android.app.Activity;
import android.content.Context;

import com.bilibili.socialize.share.core.BiliShare;
import com.bilibili.socialize.share.core.BiliShareConfiguration;
import com.bilibili.socialize.share.core.SocializeListeners;
import com.bilibili.socialize.share.core.SocializeMedia;
import com.bilibili.socialize.share.core.shareparam.BaseShareParam;
import com.bilibili.socialize.share.download.AbsImageDownloader;
import com.bilibili.socialize.share.util.FileUtil;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

/**
 * Created by jasontsang on 5/25/17.
 */

public class ShareHelper {
    private final static ShareListener shareListener = new ShareListener();

    public static BiliShareConfiguration configuration;

    public static void initialize(Context context) {
        ShareHelper.configuration = new BiliShareConfiguration.Builder(context)
                .imageDownloader(new ShareFrescoImageDownloader())
                .build();
        BiliShare.global().config(configuration);
    }

    public static void share(Activity activity, BaseShareParam param) {
        BiliShare.global().share(activity, SocializeMedia.GENERIC, param, shareListener);
    }

    public static void share(Activity activity, BaseShareParam param, ShareListener shareListener) {
        BiliShare.global().share(activity, SocializeMedia.GENERIC, param, shareListener);
    }

    public static class ShareListener implements SocializeListeners.ShareListener {
        @Override
        public void onStart(SocializeMedia type) {

        }

        @Override
        public void onProgress(SocializeMedia type, String progressDesc) {

        }

        @Override
        public void onSuccess(SocializeMedia type, int code) {

        }

        @Override
        public void onError(SocializeMedia type, int code, Throwable error) {
            Timber.e(error);
        }

        @Override
        public void onCancel(SocializeMedia type) {

        }
    }

    public static class ShareFrescoImageDownloader extends AbsImageDownloader {

        @Override
        protected void downloadDirectly(final String imageUrl, final String filePath, final OnImageDownloadListener listener) {
            if (listener != null)
                listener.onStart();

            final ImageRequest request = ImageRequest.fromUri(imageUrl);
            DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline().fetchDecodedImage(request, null);

            dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {

                @Override
                protected void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                    if (!dataSource.isFinished()) {
                        return;
                    }
                    CloseableReference<CloseableImage> ref = dataSource.getResult();
                    if (ref != null) {
                        try {
                            ImageRequest imageRequest = ImageRequest.fromUri(imageUrl);
                            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                                    .getEncodedCacheKey(imageRequest, null);
                            BinaryResource resource = Fresco.getImagePipelineFactory()
                                    .getMainFileCache()
                                    .getResource(cacheKey);
                            if (resource instanceof FileBinaryResource) {
                                File cacheFile = ((FileBinaryResource) resource).getFile();
                                try {
                                    FileUtil.copyFile(cacheFile, new File(filePath));
                                    if (listener != null) {
                                        listener.onSuccess(filePath);
                                    }
                                } catch (IOException e) {
                                    Timber.e(e, "ShareFrescoImageDownloader exception");
                                }
                            }
                        } finally {
                            CloseableReference.closeSafely(ref);
                        }
                    } else if (listener != null) {
                        listener.onFailed(imageUrl);
                    }
                }

                @Override
                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                    if (listener != null) {
                        listener.onFailed(imageUrl);
                    }
                }

            }, UiThreadImmediateExecutorService.getInstance());
        }

    }
}
