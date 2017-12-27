package com.dgtech.sss.viewdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by sss on 2017/12/25.
 */

public class RulerView extends View {

    private Paint paint;
    private int leftNum;
    private int rightNum;
    private int rightScale;
    private int leftScale;
    private int minScale ;//开始刻度
    private int maxScale ;//最大刻度
    private int defaultScale = -1;//默认指针的刻度
    private String unit;//单位
    private ScaleListener listener;//刻度变化监听

    public RulerView(Context context) {
        this(context, null);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RulerView);
        minScale = a.getInteger(R.styleable.RulerView_minScale,0)*smallScaleNum;
        maxScale = a.getInteger(R.styleable.RulerView_maxScale,50)*smallScaleNum;
        defaultScale = a.getInteger(R.styleable.RulerView_defaultScale,(minScale+maxScale)/2/smallScaleNum)*smallScaleNum;
        unit = a.getString(R.styleable.RulerView_unit);
        if (unit==null || TextUtils.isEmpty(unit)){
            unit = "kg";
        }
        a.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int h = 0;
        int w = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            w = width;
        } else {
            w = 800;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            h = height;
        } else {
            h = 800;
        }
        setMeasuredDimension(w, h);
    }

    private int smallScaleNum = 10;//大刻度间的小刻度数
    private int startX;
    private int bigY = 40;//大刻度长度
    private int smaleY = 20;
    private int smallWidth = 20;//刻度之间的最小距离
    private int start = minScale;//开始画的刻度
    private int end = maxScale;//结束画刻度

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(5);
        paint.setTextSize(60);
        paint.setColor(Color.parseColor("#2200ff00"));
        int allNum = getMeasuredWidth()/smallWidth;//最多刻度数
        rightScale = maxScale-defaultScale;//中间刻度右侧还有多少个刻度
        leftScale = defaultScale-minScale;//中间刻度左侧还有多少个刻度
        leftNum = allNum/2;
        rightNum = allNum - leftNum;
        int defaultStart = defaultScale- leftNum;
        start = defaultStart-moveScaleNum;
        int defaultEnd = defaultScale+ rightNum;
        end = defaultEnd-moveScaleNum;
        if (start<minScale){
            startX=-(start-minScale)*smallWidth;
        }else {
            startX=0;
        }
        if (start<=minScale){
            start = minScale;
        }
        if (end>=maxScale){
            end = maxScale;
        }


        //背景
        RectF rect = new RectF(0, getMeasuredHeight()/2, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRect(rect, paint);
        //上面的一条线
        paint.setColor(Color.parseColor("#919191"));
        canvas.drawLine(0,getMeasuredHeight()/2,getMeasuredWidth(),getMeasuredHeight()/2,paint);
        //画刻度
        for (int i = start;  i<= end; i++) {
            paint.setColor(Color.parseColor("#919191"));
            if (i%10==0){
                //长刻度
                canvas.drawLine(startX,getMeasuredHeight()/2,startX,getMeasuredHeight()/2+bigY,paint);
                paint.setColor(Color.BLACK);
                canvas.drawText(i/10+"",startX,getMeasuredHeight()/2+bigY+60,paint);
            }else {
                //短刻度
                canvas.drawLine(startX,getMeasuredHeight()/2,startX,getMeasuredHeight()/2+smaleY,paint);
            }
            startX+=smallWidth;
        }
        //标尺线
        paint.setStrokeWidth(10);
        paint.setColor(Color.GREEN);
        canvas.drawLine(getMeasuredWidth()/2,getMeasuredHeight()/2,getMeasuredWidth()/2, (float) (getMeasuredHeight()*0.75),paint);
        //文字
        paint.setColor(Color.BLACK);
        paint.setTextSize(80);
        double d = (defaultScale-moveScaleNum)*0.1f;
        paint.setTextAlign(Paint.Align.CENTER);
        String result = String .format("%.1f",d);
        if (listener!=null){
            listener.getScale(result);
        }
        canvas.drawText(result,getMeasuredWidth()/2,(float) (getMeasuredHeight()*0.9),paint);
        //画单位
        paint.setTextAlign(Paint.Align.LEFT);
        float widthOfResult = paint.measureText(result);
        paint.setTextSize(40);
        canvas.drawText(unit,getMeasuredWidth()/2+widthOfResult/2+10, (float) (getMeasuredHeight()*0.9),paint);

    }

    private int totalMove = 0;//总的移动量
    private int moveScaleNum=0;//一共移动多少刻度,大于0说明向右滑动
    private int fromX=0;
    private int xSpeed;//x方向速度
    private VelocityTracker velocityTracker = VelocityTracker.obtain();
    ValueAnimator animator = new ValueAnimator();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.computeCurrentVelocity(500);
        velocityTracker.addMovement(event);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                fromX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int toX = (int) event.getX();
                moveScaleNum = (totalMove+toX-fromX)/smallWidth;
//                System.out.println(moveScaleNum+"  "+leftScale+"  "+rightScale);
                if (moveScaleNum<0){
                    //向左移动
                    if (-moveScaleNum> rightScale){
                        moveScaleNum = -rightScale;
                    }
                }else {
                    if (moveScaleNum>leftScale){
                        moveScaleNum = leftScale;
                    }
                }
                System.out.println(moveScaleNum);
                break;
            case MotionEvent.ACTION_UP:
                int toX2 = (int) event.getX();
                //左滑为负值,右滑为正
                xSpeed = (int) velocityTracker.getXVelocity();
                totalMove = totalMove+toX2-fromX;
                autoScroll(xSpeed);
                velocityTracker.clear();
                break;

        }
        invalidate();
        return true;
    }
    private int value;
    //动画的核心方法,监听动画执行程度,设置moveScaleNum
    private void autoScroll(int xSpeed) {
        System.out.println(xSpeed+"   s");
        if (Math.abs(xSpeed)<200){
            return;
        }
        if (animator.isRunning()){
            return;
        }
        animator = ValueAnimator.ofInt(0,xSpeed/200).setDuration(Math.abs(xSpeed / 10));
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (int) animation.getAnimatedValue();
                moveScaleNum+= value;
                if (moveScaleNum<0){
                    //向左移动
                    if (-moveScaleNum> rightScale){
                        moveScaleNum = -rightScale;
                    }
                }else {
                    if (moveScaleNum>leftScale){
                        moveScaleNum = leftScale;
                    }
                }
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter(){

            @Override
            public void onAnimationEnd(Animator animation) {
                totalMove = moveScaleNum*smallWidth;
                animator.cancel();
                invalidate();
            }
        });
        animator.start();
    }
    //设置指针位置的值
    public void setScale(int scale){
        this.defaultScale = scale;
        invalidate();
    }
    public void setMinScale(int minScale){
        this.minScale = minScale;
        invalidate();
    }
    public void setMaxScale(int maxScale){
        this.maxScale = maxScale;
        invalidate();
    }
    public void setListener(ScaleListener listener){
        this.listener = listener;
    }
    interface ScaleListener{
        void getScale(String scale);
    }
}
