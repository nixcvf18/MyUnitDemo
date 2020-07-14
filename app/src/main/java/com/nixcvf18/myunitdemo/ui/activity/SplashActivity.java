package com.nixcvf18.myunitdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.nixcvf18.myunitdemo.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView tv_version = findViewById(R.id.tv_version);
        //使用了语法糖
        //在一秒后  在主线程中 执行jumpToMainActivity()函数
        //this  代表本类的实例     jumpToMainActivity为函数名称  ::使当前函数成为一个runnable接口
        tv_version.postDelayed(this::jumpToMainActivity,1000);





    }


    private void jumpToMainActivity() {


        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();

    }
}