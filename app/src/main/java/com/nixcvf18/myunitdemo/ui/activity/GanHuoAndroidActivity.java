package com.nixcvf18.myunitdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.nixcvf18.myunitdemo.R;
import com.nixcvf18.myunitdemo.data.GHADetailsBean;
import com.nixcvf18.myunitdemo.net.ApiService;
import com.nixcvf18.myunitdemo.utils.RxSchedulerUtil;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GanHuoAndroidActivity extends AppCompatActivity {

    private  String  ganhuoString;
    private ArrayList<GHADetailsBean> ghaDetailsBeans;
    private  GHADetailsBean  detailsBean;
    // 一次性用品的 容器
    private CompositeDisposable compositeDisposable;

    //  描述    作者      种类        邮箱      正文
    private TextView tv_desc,tv_author,tv_category,tv_email,tv_markdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gan_huo_android);

        //创建一个空的  一次性用品容器
        compositeDisposable = new CompositeDisposable();

        ghaDetailsBeans = new ArrayList<>();
        Slidr.attach(this);

        //初始化视图
        initView();
        //初始化数据
        initData();








    }

    private void initView() {
        tv_author = findViewById(R.id.tv_author);
        tv_category = findViewById(R.id.tv_category);
        tv_desc = findViewById(R.id.tv_desc);
        tv_email = findViewById(R.id.tv_email);
        tv_markdown= findViewById(R.id.tv_markdown);



    }



    private void initData() {

        ganhuoString=getIntent().getStringExtra("true_id");

        //返回用flowable 包裹的数据集合
        Disposable subscribe = ApiService.getInstance().apis.fetchGHADetailsBean(ganhuoString)
                .subscribeOn(Schedulers.io())
                //在主线程中 观察 flowable
                .observeOn(AndroidSchedulers.mainThread())

                //返回一个 一次性的资源
                .subscribe(data -> {
                    if (data != null && data.getData() != null ) {
                        detailsBean = data.getData();

                        tv_desc.setText(detailsBean.getDesc());
                        tv_author.setText("作者:"+detailsBean.getAuthor());
                        tv_email.setText(detailsBean.getEmail());
                        tv_category.setText("种类:"+detailsBean.getCategory());
                        tv_markdown.setText(detailsBean.getMarkdown());

                    }
                }, RxSchedulerUtil::processRequestException);
        //向一次性容器里  添加  一次性资源
        compositeDisposable.add(subscribe);





    }












}