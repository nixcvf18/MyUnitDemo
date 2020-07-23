package com.nixcvf18.myunitdemo.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nixcvf18.myunitdemo.R;
import com.nixcvf18.myunitdemo.data.GanHuoAndroidBean;
import com.nixcvf18.myunitdemo.net.ApiService;
import com.nixcvf18.myunitdemo.ui.adapter.GanHuoAndroidBeanAdapter;
import com.nixcvf18.myunitdemo.utils.RxSchedulerUtil;
import com.nixcvf18.myunitdemo.utils.ToastUtil;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GanHuoAndroidBeanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GanHuoAndroidBeanFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    // 一次性用品的 容器
    private CompositeDisposable compositeDisposable;
    private int currentPage = 1;
    private GanHuoAndroidBeanAdapter androidBeanAdapter;

    private ArrayList<GanHuoAndroidBean> androidBeans;
    private Interpolator interpolator = new FastOutSlowInInterpolator();

    public GanHuoAndroidBeanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @paramparam1 Parameter 1.
     * @paramparam2 Parameter 2.
     * @return A new instance of fragment GanHuoAndroidBeanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GanHuoAndroidBeanFragment newInstance() {
        GanHuoAndroidBeanFragment fragment = new GanHuoAndroidBeanFragment();

        return fragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        compositeDisposable.clear();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //创建一个空的  一次性用品容器
        compositeDisposable = new CompositeDisposable();

        androidBeans = new ArrayList<>();


        androidBeanAdapter = new GanHuoAndroidBeanAdapter(getActivity(), androidBeans);


        //将适配器与控件进行绑定
        recyclerView.setAdapter(androidBeanAdapter);

        //进行刷新操作
        swipeRefreshLayout.setRefreshing(true);


       // fetchGirlBean(true);

        fetchGanHuoAndroidBean(true);








    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gan_huo_android_bean, container, false);


        //实例化控件
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recyclerview_gan_huo_android_bean);
        floatingActionButton = view.findViewById(R.id.floatingActionButton_gan_huo_android_bean);




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;


                fetchGanHuoAndroidBean(true);
            }
        });




        //创建一个 2列的网格布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        //给recyclerview 设置布局管理器（网格布局管理器）
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when RecyclerView's scroll state changes.
             *
             * @param recyclerView The RecyclerView whose scroll state has changed.
             * @param newState     The updated scroll state. One of {@link#SCROLL_STATE_IDLE},
             *                     {@link#SCROLL_STATE_DRAGGING} or {@link#SCROLL_STATE_SETTLING}.
             */
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //返回与当前recyclerView 相绑定的布局管理器
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //如果当前处于静止状态
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    Log.d("linearLayoutManager1","当前适配器中共有"+linearLayoutManager.getItemCount());
                    Log.d("recyclerView","当前recyclerview中展示了"+recyclerView.getChildCount());
                    Log.d("linearLayoutManager2",""+linearLayoutManager.findFirstVisibleItemPosition());




                    //判断当前数据是否为最后一条 如果是的话就调用加载更多的函数
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == androidBeanAdapter.getItemCount() - 1) {

                        ++currentPage;
                        fetchGanHuoAndroidBean(true);


                    }




              /*      //  被recyclerview绑定的适配器中的条目总数20  减去    处于recyclerview控件内的view的数目6       小于等于  第一次出现在Recyclerview控件中的条目的数量
                    if (linearLayoutManager.getItemCount() - recyclerView.getChildCount()<= linearLayoutManager.findFirstVisibleItemPosition()) {


                        ++currentPage;


                        fetchGanHuoAndroidBean(false);


                    }*/


                }

            }
        });


floatingActionButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        //获取当前的布局管理器
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();


        if (linearLayoutManager.findFirstVisibleItemPosition() >30) {

            //平滑滚动到适配器中位置 是零的地方
            recyclerView.smoothScrollToPosition(0);


        } else {

            //直接界面 闪烁以下 滚动到 适配器中位置  是零的地方
            recyclerView.scrollToPosition(0);

            //按钮执行  消失动画
            floatingActionButtonOutAnim();


        }



    }
});















        return view;

    }

    private void floatingActionButtonInAnim() {

        //如果悬浮按钮 是不可见状态   那么就让它变得可见
        if (floatingActionButton.getVisibility() == View.GONE) {
            floatingActionButton.setVisibility(View.VISIBLE);
            ViewCompat.animate(floatingActionButton)
                    .scaleX(1.0F)
                    .scaleY(1.0F)
                    .alpha(1.0F)
                    .setInterpolator(interpolator)
                    .withLayer()
                    .setListener(null)
                    .start();


        }


    }

    private void floatingActionButtonOutAnim() {



        //如果悬浮按钮处于可见状态
        if (floatingActionButton.getVisibility() == View.VISIBLE) {
            ViewCompat.animate(floatingActionButton)
                    .scaleX(0.0F)
                    .scaleY(0.0F)
                    .alpha(0.0F)
                    .setInterpolator(interpolator)
                    .withLayer()
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {

                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            //动画结束时
                            //将悬浮按钮设置为不可见状态
                            floatingActionButton.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(View view) {

                        }
                    }).start();


        }



    }

    private void fetchGanHuoAndroidBean(boolean isRefresh) {

        //返回用flowable 包裹的数据集合
        Disposable subscribe = ApiService.getInstance().apis.fetchGanHuoAndroidBean(currentPage, 50)
                .subscribeOn(Schedulers.io())
                //在主线程中 观察 flowable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> swipeRefreshLayout.setRefreshing(true))
                //在flowable 发送数据集合成功后   设置当前不执行下拉刷新控件的刷新操作
                .doFinally(() -> swipeRefreshLayout.setRefreshing(false))
                //返回一个 一次性的资源
                .subscribe(data -> {
                    if (data != null && data.getData() != null && data.getData().size() > 0) {
                        ArrayList<GanHuoAndroidBean> results = data.getData();
                        if (isRefresh) {
                           androidBeanAdapter.addAll(results);
                            ToastUtil.shortToast("刷新成功");
                        } else {
                            androidBeanAdapter.loadMore(results);
                            String msg = String.format("嘤嘤嘤，新增%1$d篇%2$s", results.size(), "干货");
                            ToastUtil.shortToast(msg);
                        }
                    }
                }, RxSchedulerUtil::processRequestException);
        //向一次性容器里  添加  一次性资源
        compositeDisposable.add(subscribe);



    }
}