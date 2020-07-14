package com.nixcvf18.myunitdemo;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

public class UnitDemoInit {
    private static Long  HTTP_CONNECT_TIMEOUT = 30L;
    private static Long  HTTP_WRITE_TIMEOUT = 30L;
    private static Long  HTTP_READ_TIMEOUT = 30L;

    public static OkHttpClient  okHttpClient;


    static void initOkHttp(Application application) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Timber.tag("OkHttps").d(message));
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging);
       okHttpClient = builder.build();




    }



    /* 初始化Timber */
    static void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }


}
