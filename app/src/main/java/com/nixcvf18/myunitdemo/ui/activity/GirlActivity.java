package com.nixcvf18.myunitdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Slide;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nixcvf18.myunitdemo.R;
import com.r0adkll.slidr.Slidr;

public class GirlActivity extends AppCompatActivity {

    private ImageView imageViewBig;
    private  String   imageUrl;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girl);

        Slidr.attach(this);
        initData();
        initView();
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
}