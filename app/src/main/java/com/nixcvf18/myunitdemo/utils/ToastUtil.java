package com.nixcvf18.myunitdemo.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.nixcvf18.myunitdemo.BaseApplication;

public class ToastUtil {



    public static void shortToast(String msg) {
        Toast toast = Toast.makeText(BaseApplication.context,msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 40);
        toast.show();
    }

    public static void longToast(String msg) {
        Toast toast = Toast.makeText(BaseApplication.context,msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 40);
        toast.show();
    }
}
