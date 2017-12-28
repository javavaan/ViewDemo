package com.dgtech.sss.viewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by sss on 2017/12/27.
 * 本来是水桶,结果用canvas一切,就成了水球
 */

public class CircleWaveView extends WaveView {
    public CircleWaveView(Context context) {
        this(context,null);
    }

    public CircleWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        if (w>=h) {
            setMeasuredDimension(h, h);
        }else {
            setMeasuredDimension(w, w);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int width = getMeasuredWidth();
        path.addCircle(width/2,width/2,width/2, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);

    }
}
