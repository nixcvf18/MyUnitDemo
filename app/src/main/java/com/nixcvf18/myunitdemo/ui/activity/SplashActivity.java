package com.nixcvf18.myunitdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.nixcvf18.myunitdemo.R;

public class SplashActivity extends AppCompatActivity {

    private  MyCountDownTimer myCountDownTimer;
    private  TextView tv_count_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView tv_version = findViewById(R.id.tv_version);
        tv_count_time = findViewById(R.id.tv_count_time);
        myCountDownTimer = new MyCountDownTimer(3000, 1000);
        myCountDownTimer.start();

        //使用了语法糖
        //在一秒后  在主线程中 执行jumpToMainActivity()函数
        //this  代表本类的实例     jumpToMainActivity为函数名称  ::使当前函数成为一个runnable接口
        tv_version.postDelayed(this::jumpToMainActivity,3000);





    }


    private void jumpToMainActivity() {


        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();

    }


    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * Callback fired on regular interval.
         *
         * @param millisUntilFinished The amount of time until finished.
         */
        @Override
        public void onTick(long millisUntilFinished) {

            tv_count_time.setText(" " + (millisUntilFinished / 1000+1));
        }

        /**
         * Callback fired when the time is up.
         */
        @Override
        public void onFinish() {

            tv_count_time.setText("");
        }
    }


}
