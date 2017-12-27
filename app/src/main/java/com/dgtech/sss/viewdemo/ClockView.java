package com.dgtech.sss.viewdemo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.util.Map;

/**
 * Created by sss on 2017/12/21.
 */

public class ClockView extends AppCompatTextView {

    private Map<String, Integer> datas;
    private Camera camera = new Camera();

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }



    Paint paint = new Paint();
    ObjectAnimator animator = ObjectAnimator.ofInt(this,"degree",0,60);

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
        invalidate();
    }


    int l = 50;
    int s = 25;
    int clock = 0;
    int degree=0;//指针旋转的角度
    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    degree++;
                    invalidate();
                    handler.sendEmptyMessageDelayed(0,1000);
                    break;
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {

        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(40);
        int radius = getMeasuredWidth()/4;
       canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,radius,paint);

       //画刻度
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(6);
        for (int i = 0; i <360; i=i+6) {
            paint.setStrokeWidth(8);
            if (i%5==0){
                //长刻度
                float startX = (float) ((radius-l)*Math.sin(Math.toRadians(i))+getMeasuredWidth()/2);
                float endX = (float) (radius*Math.sin(Math.toRadians(i))+getMeasuredWidth()/2);
                float startY = (float) (getMeasuredHeight()/2-(radius-l)*Math.cos(Math.toRadians(i)));
                float endY = (float) (getMeasuredHeight()/2-radius*Math.cos(Math.toRadians(i)));
                canvas.drawLine(startX,startY,endX,endY,paint);
                paint.setStrokeWidth(1);
                paint.setStyle(Paint.Style.FILL);
                float startCX = (float) ((radius-l-40)*Math.sin(Math.toRadians(i))+getMeasuredWidth()/2);
                float startCY = (float) (getMeasuredHeight()/2-(radius-l-40)*Math.cos(Math.toRadians(i)));
                canvas.drawText(clock+"",startCX,startCY,paint);
                clock++;
                if (clock==12){
                    clock=0;
                }
            }else {
                //短刻度
                paint.setStrokeWidth(6);
                float startX = (float) ((radius-s)*Math.sin(Math.toRadians(i))+getMeasuredWidth()/2);
                float endX = (float) (radius*Math.sin(Math.toRadians(i))+getMeasuredWidth()/2);
                float startY = (float) (getMeasuredHeight()/2-(radius-s)*Math.cos(Math.toRadians(i)));
                float endY = (float) (getMeasuredHeight()/2-radius*Math.cos(Math.toRadians(i)));
                canvas.drawLine(startX,startY,endX,endY,paint);
            }
        }
        //上面完成了表盘,下面画指针
        paint.setStrokeWidth(10);
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,10,paint);//指针的圆点
        //秒针
        float endSecondX = (float) (radius*0.5*Math.sin(Math.toRadians(degree*6))+getMeasuredWidth()/2);
        float endSecondY = (float) (getMeasuredHeight()/2-radius*0.5*Math.cos(Math.toRadians(degree*6)));
        canvas.drawLine(getMeasuredWidth()/2,getMeasuredHeight()/2,endSecondX,endSecondY,paint);
        //分针
        float endMinX = (float) (radius*0.45*Math.sin(Math.toRadians(degree*1.0/10))+getMeasuredWidth()/2);
        float endMinY = (float) (getMeasuredHeight()/2-radius*0.45*Math.cos(Math.toRadians(degree*1.0/10)));
        canvas.drawLine(getMeasuredWidth()/2,getMeasuredHeight()/2,endMinX,endMinY,paint);
        //时针
        float endHourX = (float) (radius*0.35*Math.sin(Math.toRadians(degree*1.0/600))+getMeasuredWidth()/2);
        float endHourY = (float) (getMeasuredHeight()/2-radius*0.35*Math.cos(Math.toRadians(degree*1.0/600)));
        canvas.drawLine(getMeasuredWidth()/2,getMeasuredHeight()/2,endHourX,endHourY,paint);

        int h = degree/3600%24;
        int m = degree/60%60;;
        int s = degree%60;
        String hh = "";
        String mm = "";
        String ss = "";

        if (h<10){
            hh="0"+h;
        }else {
            hh = ""+h;
        }
        if (m<10){
            mm = "0"+m;
        }else {
            mm = ""+m;
        }
        if (s<10){
            ss = "0"+s;
        }else {
            ss = ""+s;
        }
        String time = hh+" : "+mm+" : "+ss;
        canvas.drawText(time,getMeasuredWidth()/2,getMeasuredHeight()/2+60,paint);
    }

    public void stop(){
        degree=0;
        handler.removeCallbacksAndMessages(null);
        invalidate();
    }
    public void pause(){
        handler.removeCallbacksAndMessages(null);
        invalidate();
    }
    public void start(){
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessageDelayed(0,1000);
    }
    public void setTime(int h,int m,int s){
        degree = h*3600+m*60+s;
        invalidate();
    }
}
