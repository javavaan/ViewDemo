package com.dgtech.sss.viewdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by sss on 2017/12/27.
 */

public class WaveView extends View implements View.OnClickListener {

    private Paint paint;
    private Path path;
    private int waveLen;
    private ObjectAnimator animator;

    public WaveView(Context context) {
        this(context,null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        path = new Path();
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);


        this.setOnClickListener(this);

    }

    private float degree = (float) 0.1;
    private int offsetX = 0;//x偏移量
    private int offsetYDefault = 100;
    private int offsetY = offsetYDefault;//y偏移量,也就是波峰的高度
    private int animCount = 0;
    public void setOffsetX(int offsetX){
        this.offsetX = offsetX;
        //offsetY越来越小,实现波纹变平静的效果
        offsetY=offsetYDefault-animCount*10;
        if (offsetY<=10){
            offsetY=10;
        }
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        waveLen = getMeasuredWidth();
        //重点就是在该view的可见区域左侧画一个和可见区域一样的正弦或者余弦图,图形向右移动即可实现波浪效果
        path.reset();
        int centerY = (int) (getMeasuredHeight()*(1-degree));
        path.moveTo(-getMeasuredWidth()+ offsetX,centerY);
        path.cubicTo(offsetX - waveLen /2,centerY-offsetY, offsetX - waveLen /2,centerY+offsetY, offsetX +0,centerY);
        path.cubicTo(offsetX + waveLen /2,centerY-offsetY, offsetX + waveLen /2,centerY+offsetY, offsetX + waveLen,centerY);
        path.lineTo(waveLen, (float) (getMeasuredHeight()));
        path.lineTo(0, (float) (getMeasuredHeight()));
        path.close();
        canvas.drawPath(path,paint);
    }

 /*   @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //注意:一定要设置差值器,否则使用默认的差值器AccelerateDecelerateInterpolator(先加速后减速)

                break;
        }

        //两种方法都行
       *//* ValueAnimator animator = ValueAnimator.ofInt(0, waveLen);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offsetX = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();*//*
        return true;
    }*/

    @Override
    public void onClick(View v) {

//        if (animator.isRunning()){
//            return;
//        }
//        animator.start();
    }
    public void setDegree(float degree){
        this.clearAnimation();
        this.degree = degree;
        offsetY=offsetYDefault;
        animCount=0;
        invalidate();
        if (animator==null) {
            animator = ObjectAnimator.ofInt(this, "offsetX", 0, waveLen).setDuration(2000);
            animator.setRepeatCount(-1);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatMode(ValueAnimator.RESTART);
        }

        animator.removeAllListeners();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                animCount++;
                System.out.println(animCount);;
            }
        });

        animator.start();
    }
}
