package com.dgtech.sss.viewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private ClockView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*circleView = findViewById(R.id.time);
        findViewById(R.id.bt_start).setOnClickListener(this);
        findViewById(R.id.bt_stop).setOnClickListener(this);
        findViewById(R.id.bt_pause).setOnClickListener(this);
        findViewById(R.id.bt_set).setOnClickListener(this);*/
        final TextView result = findViewById(R.id.result);
        RulerView rulerView = findViewById(R.id.ruler);
        rulerView.setListener(new RulerView.ScaleListener() {
            @Override
            public void getScale(String scale) {
                result.setText(scale);
            }
        });
    }

    @Override
    public void onClick(View v) {
       /* switch (v.getId()){
            case R.id.bt_start:
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
                break;
        }*/
    }
}
