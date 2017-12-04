package com.github.caoyouxin.taoke.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;


public class RatioImageView extends SimpleDraweeView {

    private int originalWidth;
    private int originalHeight;
    private boolean according2width = true;

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOriginalSizeAccordingToWidth(int originalWidth, int originalHeight) {
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        this.according2width = true;

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            float ratio = (float) originalWidth / (float) originalHeight;

            int width = layoutParams.width;

            if (width > 0) {
                layoutParams.height = (int) ((float) width / ratio);
            }

            setLayoutParams(layoutParams);
        }
    }

    public void setOriginalSizeAccordingToHeight(int originalWidth, int originalHeight) {
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        this.according2width = false;

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            float ratio = (float) originalWidth / (float) originalHeight;

            int height = layoutParams.height;

            if (height > 0) {
                layoutParams.width = (int) ((float) height * ratio);
            }

            setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (originalWidth > 0 && originalHeight > 0) {
            float ratio = (float) originalWidth / (float) originalHeight;

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            if (this.according2width) {
                if (width > 0) {
                    height = (int) ((float) width / ratio);
                }
            } else {
                if (height > 0) {
                    width = (int) ((float) height * ratio);
                }
            }

            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}