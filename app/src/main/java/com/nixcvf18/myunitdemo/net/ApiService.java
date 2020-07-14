package com.nixcvf18.myunitdemo.net;

import com.nixcvf18.myunitdemo.UnitDemoInit;
import com.nixcvf18.myunitdemo.data.GirlResults;

import io.reactivex.Flowable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ApiService {

    private static String BASE_URL = "http://www.wocaoni.com/";   //未启用
    private  static  ApiService  apiService;
    //声明接口实例
public Apis  apis;


    public static ApiService getInstance() {
        if (apiService == null) {
            apiService = new ApiService();
        }

        return apiService;

    }

    private ApiService() {
        //Builder()函数 用来构建一个retrofit
        Retrofit storeRestAPI = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(UnitDemoInit.okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apis = storeRestAPI.create(Apis.class);
    }








    //声明接口
    public interface  Apis{
        //                                  (category)    (type)    (page)   (count)
        // https://gank.io/api/v2/data/category/Girl/type/Girl/page/1/count/10
        @GET("https://gank.io/api/v2/data/category/Girl/type/Girl/page/{pageNumber}/count/{countNumber}")
        Flowable<GirlResults> fecthGrilBean(

              @Path("pageNumber")  int pageNumber,
              @Path("countNumber")  int  countNumber

        );


    }
}
