package com.umeitime.common.http;

import com.umeitime.common.http.interceptor.factory.CustomGsonConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class AppClient {
    public static Retrofit mRetrofit;
    public static Retrofit retrofit(String url) {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(CustomGsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(OkHttp3Utils.getOkHttpClient())
                    .build();
        }
        return mRetrofit;
    }
}
