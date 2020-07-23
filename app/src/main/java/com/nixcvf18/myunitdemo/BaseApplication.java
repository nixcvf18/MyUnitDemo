package com.nixcvf18.myunitdemo;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

    public  static  Context context;





    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        //打印相关的日志
        UnitDemoInit.initTimber();
        UnitDemoInit.initOkHttp(this);

    }











}
