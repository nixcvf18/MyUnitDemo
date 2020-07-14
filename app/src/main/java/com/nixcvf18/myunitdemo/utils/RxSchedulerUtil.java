package com.nixcvf18.myunitdemo.utils;

import com.nixcvf18.myunitdemo.R;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxSchedulerUtil {









    public static <T> ObservableTransformer<T, T> compose() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    public static void processRequestException(Throwable e) {
        if(e instanceof ConnectException || e instanceof SocketException) {
            ToastUtil.shortToast("网络连接异常，请稍后重试！");
        } else if(e instanceof SocketTimeoutException) {
            ToastUtil.shortToast("网络请求超时，请检查网络状态！");
        } else if(e instanceof UnknownHostException) {
            ToastUtil.shortToast("当前网络不可用，请检查网络情况！");
        } else {
           //Timber.d(e.getMessage());
        }
    }


}
