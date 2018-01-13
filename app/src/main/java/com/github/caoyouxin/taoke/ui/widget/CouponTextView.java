package com.github.caoyouxin.taoke.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class CouponTextView extends android.support.v7.widget.AppCompatTextView {
    private Paint mPaint;       //画笔
    private float gap = 8;      //圆间距
    private float radius = 10;  //半径
    private int circleNum;      //圆数量
    private float remain;
    private float width;        //视图宽

    public CouponTextView(Context context) {
        super(context);
    }

    public CouponTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置是否使用抗锯齿功能，会消耗较大资源，绘制图形速度会变慢。
        mPaint.setDither(true);//设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public CouponTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        if (remain==0){
            //计算不整除的剩余部分
            remain = (int)(h-gap)%(2*radius+gap);
        }
        circleNum = (int) ((h-gap)/(2*radius+gap));  //计算圆的数量
    }

    /**
     * 上面定义了圆的半径和圆间距，同时初始化了这些值并且获取了需要画的圆数量。
     接下来只需要一个一个将圆画出来就可以了。
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //循环在左右两个边上画出凹凸效果
        for (int i=0;i<circleNum;i++){
            float y = gap+radius+remain/2+((gap+radius*2)*i);//计算出y轴坐标
            canvas.drawCircle(0,y,radius,mPaint);//在左边边画圆
            canvas.drawCircle(width,y,radius,mPaint);//在右边画圆
        }
    }
}
