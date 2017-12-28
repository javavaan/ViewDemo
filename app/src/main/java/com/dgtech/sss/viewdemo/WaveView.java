package com.dgtech.sss.viewdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by sss on 2017/12/27.
 * 可以实现波纹越来越小的效果,用的handler发送延迟消息
 * 两种实现动画的方式,虽然都是属性动画,一个是用ObjectAnimator来改变属性值,一个使用ValueAnimator监听动画执行程度来做相应的事情
 * onAnimationRepeat这个监听有点诡异
 */

public class WaveView extends View implements View.OnClickListener {

    private Paint paint;
    private Path path;
    private int waveLen;
    private ObjectAnimator animator;
    private int durTiem = 1000;//执行一次动画需要的时间

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
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
    private boolean ifNoAnim = true;

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
        //offsetY越来越小,实现波纹变平静的效果
//        offsetY=offsetYDefault-animCount*10;
        if (offsetY <= 20) {
            offsetY = 20;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        waveLen = getMeasuredWidth();
        //重点就是在该view的可见区域左侧画一个和可见区域一样的正弦或者余弦图,图形向右移动即可实现波浪效果
        path.reset();
        int centerY = (int) (getMeasuredHeight() * (1 - degree));
        path.moveTo(-getMeasuredWidth() + offsetX, centerY);
        path.cubicTo(offsetX - waveLen / 2, centerY - offsetY, offsetX - waveLen / 2, centerY + offsetY, offsetX + 0, centerY);
        path.cubicTo(offsetX + waveLen / 2, centerY - offsetY, offsetX + waveLen / 2, centerY + offsetY, offsetX + waveLen, centerY);
        path.lineTo(waveLen, (float) (getMeasuredHeight()));
        path.lineTo(0, (float) (getMeasuredHeight()));
        path.close();
        canvas.drawPath(path, paint);
        //如果用户还没有执行过动画,就执行动画
        if (ifNoAnim){
            ifNoAnim = false;
            offsetY=offsetYDefault;
            animator = ObjectAnimator.ofInt(this, "offsetX", 0, waveLen).setDuration(durTiem);
            animator.setRepeatCount(-1);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.start();
            handler.sendEmptyMessage(0);
        }
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

    private android.os.Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    offsetY -= 5;
                    handler.sendEmptyMessageDelayed(0, durTiem);
                    break;
            }

        }
    };

    public void setDegree(float degree) {
        ifNoAnim = false;
        this.clearAnimation();
        this.degree = degree;
        offsetY = offsetYDefault;
        invalidate();
        if (animator == null) {
            animator = ObjectAnimator.ofInt(this, "offsetX", 0, waveLen).setDuration(durTiem);
            animator.setRepeatCount(-1);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatMode(ValueAnimator.RESTART);
        }

        //这个方法获取动画重复的次数总是有问题
      /*  animator.removeAllListeners();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                animCount++;
                System.out.println(animCount);;
            }
        });*/
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessage(0);
        animator.start();
    }
}
