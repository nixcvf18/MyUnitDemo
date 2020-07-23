package com.nixcvf18.myunitdemo.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nixcvf18.myunitdemo.R;
import com.nixcvf18.myunitdemo.data.GHADetailsBean;
import com.nixcvf18.myunitdemo.data.GirlBean;
import com.nixcvf18.myunitdemo.net.ApiService;
import com.nixcvf18.myunitdemo.utils.RxSchedulerUtil;
import com.nixcvf18.myunitdemo.utils.ToastUtil;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GHADetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GHADetailsFragment extends Fragment {


    private  String  trueUrl;
    private ArrayList<GHADetailsBean> ghaDetailsBeans;
    private  GHADetailsBean  detailsBean;
    // 一次性用品的 容器
    private CompositeDisposable compositeDisposable;

                //  描述    作者      种类        邮箱      正文
    private TextView tv_desc,tv_author,tv_category,tv_email,tv_markdown;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @paramparam1 Parameter 1.
     * @paramparam2 Parameter 2.
     * @return A new instance of fragment GHADetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GHADetailsFragment newInstance() {
        GHADetailsFragment fragment = new GHADetailsFragment();

        return fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //创建一个空的  一次性用品容器
        compositeDisposable = new CompositeDisposable();

        ghaDetailsBeans = new ArrayList<>();


    




        fetchGHADetailsBean();






    }

    private void fetchGHADetailsBean() {

        //返回用flowable 包裹的数据集合
        Disposable subscribe = ApiService.getInstance().apis.fetchGHADetailsBean(trueUrl)
                .subscribeOn(Schedulers.io())
                //在主线程中 观察 flowable
                .observeOn(AndroidSchedulers.mainThread())

                //返回一个 一次性的资源
                .subscribe(data -> {
                    if (data != null && data.getData() != null ) {
                        detailsBean = data.getData();

                        tv_desc.setText(detailsBean.getDesc());
                        tv_author.setText(detailsBean.getAuthor());
                        tv_email.setText(detailsBean.getEmail());
                        tv_category.setText(detailsBean.getCategory());
                        tv_markdown.setText(detailsBean.getMarkdown());

                    }
                }, RxSchedulerUtil::processRequestException);
        //向一次性容器里  添加  一次性资源
        compositeDisposable.add(subscribe);






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



             View view=   inflater.inflate(R.layout.fragment_g_h_a_details, container, false);
        tv_author = view.findViewById(R.id.tv_author);
        tv_category = view.findViewById(R.id.tv_category);
        tv_desc = view.findViewById(R.id.tv_desc);
        tv_email = view.findViewById(R.id.tv_email);
        tv_markdown = view.findViewById(R.id.tv_markdown);


        fetchGHADetailsBean();




        return view;





    }
}