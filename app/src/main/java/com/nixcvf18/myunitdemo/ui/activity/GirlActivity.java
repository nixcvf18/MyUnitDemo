package com.nixcvf18.myunitdemo.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.nixcvf18.myunitdemo.R;
import com.r0adkll.slidr.Slidr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GirlActivity extends AppCompatActivity {

    private ImageView imageViewBig;
    private String imageUrl;
    private View view;

    private TextView savePhoto;
    private TextView cancel;
    private Dialog dialog;

    String imageName = null;
    File outPutImage = null;
    FileOutputStream fileOutputStream = null;

    Handler handler = new Handler() {


        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 001:
                    Toast.makeText(GirlActivity.this, "图片保存成功,您将图片" + imageUrl + "保存到了" + outPutImage, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(GirlActivity.this, "图片保存成功", Toast.LENGTH_LONG).show();

                    break;


                default:


                    break;


            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girl);

        Slidr.attach(this);
        //初始化数据
        initData();
        //初始化视图
        initView();


        imageViewBig.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                // Toast.makeText(GirlActivity.this, "你长按了该图片", Toast.LENGTH_SHORT).show();
                show(v);

                return true;
            }
        });
    }


    private void initView() {

        imageViewBig = findViewById(R.id.girl_imageView_big);
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(imageViewBig);


        }
    }

    private void initData() {

        imageUrl = getIntent().getStringExtra("picture_url");

    }


    public void show(View view) {
        dialog = new Dialog(this, R.style.Theme_AppCompat_Dialog);
        //填充对话框的布局
        this.view = LayoutInflater.from(this).inflate(R.layout.dialog_save_bitmap, null);
        //初始化控件
        savePhoto = (TextView) this.view.findViewById(R.id.tv_savePhoto);
        cancel = (TextView) this.view.findViewById(R.id.tv_cancel);
        //将从1970年1月1日  到如今日期  所经过的毫秒数 作为保存在本地图片的文件名
        imageName = System.currentTimeMillis() + ".jpg";
        //使用应用程序指定目录的绝对路径 和 图片文件名 去创建一个新的的文件
        outPutImage = new File(getExternalCacheDir(), imageName);
        try {
            //创建代表该文件的  文件输出流
            fileOutputStream = new FileOutputStream(outPutImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        savePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        //从指定的网络地址 imageUrl 构建一个Bitmap请求构造器
                        FutureTarget<Bitmap> bitmapFutureTarget = Glide.with(GirlActivity.this)
                                .asBitmap()
                                .load(imageUrl)
                                .submit();

                        try {
                            //从加载器中返回Bitmap对象
                            Bitmap bitmap = bitmapFutureTarget.get();
                            //将输出流 压缩成jpeg格式
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                            //刷新输出流
                            fileOutputStream.flush();
                            //关闭文件输出流  并释放有关的系统资源
                            fileOutputStream.close();


                            if (outPutImage.exists()) {
                                Message message = new Message();
                                message.what = 001;
                                handler.sendMessage(message);

                            }


                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();

                //移除该对话框
                dialog.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //移除该对话框
                dialog.dismiss();

            }
        });
        //将布局设置给Dialog
        dialog.setContentView(this.view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框


    }


}