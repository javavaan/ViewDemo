package com.dgtech.sss.viewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sss on 2018/1/25.
 * onDraw方法中用了canvas的translate和rotate方法,这样操作画布的目的一是为了显示的更加好看,二是为了方便画
 * 主要练习指针扫过区域颜色渐变
 */

public class MiClock extends View {

    private Paint paint;

    public MiClock(Context context) {
        this(context, null);
    }

    public MiClock(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
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

    private int degree = 0;

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    degree++;
                    invalidate();
                    handler.sendEmptyMessageDelayed(0, 1000 / 6);
                    break;
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        canvas.drawColor(Color.BLACK);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        //直径是宽高中最小的一个再减去200
        int d = measuredWidth <= measuredHeight ? measuredWidth : measuredHeight;
        d = d - 200;
        //圆心
//        Point center = new Point(measuredWidth / 2, measuredHeight / 2);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
//        canvas.drawLine(0,measuredHeight/2,measuredWidth,measuredHeight/2,paint);
//        canvas.drawLine(measuredWidth/2,0,measuredWidth/2,measuredHeight,paint);
        //画最外圈
        canvas.drawText("12", getMeasuredWidth() / 2, getMeasuredHeight() / 2 - d / 2, paint);
        canvas.drawText("3", getMeasuredWidth() / 2 + d / 2, getMeasuredHeight() / 2, paint);
        canvas.drawText("6", getMeasuredWidth() / 2, getMeasuredHeight() / 2 + d / 2, paint);
        canvas.drawText("9", getMeasuredWidth() / 2 - d / 2, getMeasuredHeight() / 2, paint);
        RectF rectF = new RectF(measuredWidth / 2 - d / 2, measuredHeight / 2 - d / 2, measuredWidth / 2 + d / 2, measuredHeight / 2 + d / 2);
        //如果不设置style画出来的不是线
        paint.setStyle(Paint.Style.STROKE);
//        canvas.save();
        canvas.translate(0, -20);
        canvas.drawArc(rectF, 5, 80, false, paint);
        canvas.drawArc(rectF, 95, 80, false, paint);
        canvas.drawArc(rectF, 185, 80, false, paint);
        canvas.drawArc(rectF, 275, 80, false, paint);
        canvas.save();
//        canvas.restore();
        //画小刻度 360个
        int degreeLen = 50;//刻度长度
        d = d - 100;
        int inR = d / 2 - degreeLen;
        canvas.rotate(-90, measuredWidth / 2, measuredHeight / 2);
        paint.setColor(Color.parseColor("#717105"));
        for (int i = 0; i < 360; i++) {
            int degree2 = degree % 360;
            if (degree2 >= 5) {
                if (i == degree2) {
                    paint.setColor(Color.parseColor("#ffff00"));
                } else if (i == degree2 - 1) {
                    paint.setColor(Color.parseColor("#dcdc08"));
                } else if (i == degree2 - 2) {
                    paint.setColor(Color.parseColor("#c6c607"));
                } else if (i == degree2 - 3) {
                    paint.setColor(Color.parseColor("#aeae06"));
                } else if (i == degree2 - 4) {
                    paint.setColor(Color.parseColor("#848403"));
                } else {
                    paint.setColor(Color.parseColor("#717105"));
                }
            } else if (degree2 >= 4) {
                if (i == degree2) {
                    paint.setColor(Color.parseColor("#ffff00"));
                } else if (i == degree2 - 1) {
                    paint.setColor(Color.parseColor("#dcdc08"));
                } else if (i == degree2 - 2) {
                    paint.setColor(Color.parseColor("#c6c607"));
                } else if (i == degree2 - 3) {
                    paint.setColor(Color.parseColor("#aeae06"));
                } else if (degree > 360) {
                    if (i == 0) {
                        paint.setColor(Color.parseColor("#848403"));
                    } else {
                        paint.setColor(Color.parseColor("#717105"));
                    }
                } else {
                    paint.setColor(Color.parseColor("#717105"));
                }
            } else if (degree2 >= 3) {
                if (i == degree2) {
                    paint.setColor(Color.parseColor("#ffff00"));
                } else if (i == degree2 - 1) {
                    paint.setColor(Color.parseColor("#dcdc08"));
                } else if (i == degree2 - 2) {
                    paint.setColor(Color.parseColor("#c6c607"));
                } else if (degree > 360) {
                    if (i == 0) {
                        paint.setColor(Color.parseColor("#aeae06"));
                    } else if (i == 359) {
                        paint.setColor(Color.parseColor("#848403"));
                    } else {
                        paint.setColor(Color.parseColor("#717105"));
                    }
                } else {
                    paint.setColor(Color.parseColor("#717105"));
                }
            } else if (degree2 >= 2) {
                if (i == degree2) {
                    paint.setColor(Color.parseColor("#ffff00"));
                } else if (i == degree2 - 1) {
                    paint.setColor(Color.parseColor("#dcdc08"));
                } else if (degree > 360) {
                    if (i == 0) {
                        paint.setColor(Color.parseColor("#c6c607"));
                    } else if (i == 359) {
                        paint.setColor(Color.parseColor("#aeae06"));
                    } else if (i == 358) {
                        paint.setColor(Color.parseColor("#848403"));
                    } else {
                        paint.setColor(Color.parseColor("#717105"));
                    }
                } else {
                    paint.setColor(Color.parseColor("#717105"));
                }
            } else if (degree2 >= 1) {
                if (i == degree2) {
                    paint.setColor(Color.parseColor("#ffff00"));
                } else if (degree > 360) {
                    if (i == 0) {
                        paint.setColor(Color.parseColor("#dcdc08"));
                    } else if (i == 359) {
                        paint.setColor(Color.parseColor("#c6c607"));
                    } else if (i == 358) {
                        paint.setColor(Color.parseColor("#aeae06"));
                    } else if (i == 357) {
                        paint.setColor(Color.parseColor("#848403"));
                    } else {
                        paint.setColor(Color.parseColor("#717105"));
                    }
                } else {
                    paint.setColor(Color.parseColor("#717105"));
                }
            } else if (degree2 == 0) {
                degree2 = 360;
                if (degree > 0) {
                    if (i == 0) {
                        paint.setColor(Color.parseColor("#ffff00"));
                    } else if (i == degree2 - 1) {
                        paint.setColor(Color.parseColor("#dcdc08"));
                    } else if (i == degree2 - 2) {
                        paint.setColor(Color.parseColor("#c6c607"));
                    } else if (i == degree2 - 3) {
                        paint.setColor(Color.parseColor("#aeae06"));
                    } else if (i == degree2 - 4) {
                        paint.setColor(Color.parseColor("#848403"));
                    } else {
                        paint.setColor(Color.parseColor("#717105"));
                    }
                } else {
                    paint.setColor(Color.parseColor("#717105"));
                }
            }

            float startX = (float) (inR * Math.cos(Math.toRadians(i)) + measuredWidth / 2);
            float startY = (float) (measuredHeight / 2 + inR * Math.sin(Math.toRadians(i)));
            float endX = (float) (d / 2 * Math.cos(Math.toRadians(i)) + measuredWidth / 2);
            float endY = (float) (measuredHeight / 2 + d / 2 * Math.sin(Math.toRadians(i)));
            canvas.drawLine(startX, startY, endX, endY, paint);
        }
        canvas.restore();
        //画指针
        paint.setColor(Color.WHITE);
        d = d - 100;
        paint.setStrokeWidth(8);
        canvas.drawCircle(measuredWidth / 2, measuredHeight / 2, 20, paint);
        //画三角指针
        int l = 30;//三角形边长
        Path path = new Path();
        float x1 = (float) (measuredWidth / 2 + d / 2 * Math.sin(Math.toRadians(degree)));
        float y1 = (float) (measuredHeight / 2 - d / 2 * Math.cos(Math.toRadians(degree)));
        //三角形定点
        path.moveTo(x1, y1);
        //三角形左边点
        float x2 = (float) (x1 - l * Math.cos(Math.toRadians(90 - degree - 30)));
        float y2 = (float) (y1 + l * Math.sin(Math.toRadians(90 - degree - 30)));
        path.lineTo(x2, y2);
        float x3 = (float) (x1 - l * Math.sin(Math.toRadians(degree - 30)));
        float y3 = (float) (y1 + l * Math.cos(Math.toRadians(degree - 30)));
        path.lineTo(x3, y3);
        path.close();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);
        d = d - 100;
        //画分针,时针
        float startX1 = (float) (measuredWidth / 2 + 20 * Math.sin(Math.toRadians(degree / 60)));
        float startY1 = (float) (measuredHeight / 2 - 20 * Math.cos(Math.toRadians(degree / 60)));
        float endX1 = (float) (measuredWidth / 2 + d / 2 * Math.sin(Math.toRadians(degree / 60)));
        float endY1 = (float) (measuredHeight / 2 - d / 2 * Math.cos(Math.toRadians(degree / 60)));
        d = d - 200;
        float startX2 = (float) (measuredWidth / 2 + 20 * Math.sin(Math.toRadians(degree / (60 * 60))));
        float startY2 = (float) (measuredHeight / 2 - 20 * Math.cos(Math.toRadians(degree / (60 * 60))));
        float endX2 = (float) (measuredWidth / 2 + d / 2 * Math.sin(Math.toRadians(degree / (60 * 60))));
        float endY2 = (float) (measuredHeight / 2 - d / 2 * Math.cos(Math.toRadians(degree / (60 * 60))));
        canvas.drawLine(startX1, startY1, endX1, endY1, paint);
        paint.setStrokeWidth(12);
        canvas.drawLine(startX2, startY2, endX2, endY2, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (degree == 0) {
                    handler.sendEmptyMessageDelayed(0, 1000 / 6);
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
