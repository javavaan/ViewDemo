package com.dgtech.sss.viewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private ClockView circleView;
    private WaveView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.min).setOnClickListener(this);
        /*circleView = findViewById(R.id.time);
        findViewById(R.id.bt_start).setOnClickListener(this);
        findViewById(R.id.bt_stop).setOnClickListener(this);
        findViewById(R.id.bt_pause).setOnClickListener(this);
        findViewById(R.id.bt_set).setOnClickListener(this);*/
//        final TextView result = findViewById(R.id.result);
//        RulerView rulerView = findViewById(R.id.ruler);
//        rulerView.setListener(new RulerView.ScaleListener() {
//            @Override
//            public void getScale(String scale) {
//                result.setText(scale);
//            }
//        });
        waveView = findViewById(R.id.wave);
    }

    private float degree = (float) 0.1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                degree = (float) (degree + 0.1);
                if (degree >= 1.0) {
                    degree = (float) 1.0;
                }
                waveView.setDegree(degree);
                break;
            case R.id.min:
                degree = (float) (degree - 0.1);
                if (degree<=0){
                    degree = 0;
                }
                waveView.setDegree(degree);
                break;
           /* case R.id.bt_start:
                circleView.start();
                break;
            case R.id.bt_pause:
                circleView.pause();
                break;
            case R.id.bt_stop:
                circleView.stop();
                break;
            case R.id.bt_set:
                circleView.setTime(10,22,33);
                break;*/
        }
    }
}
