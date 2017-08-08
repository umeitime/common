package com.umeitime.common.http;

import com.umeitime.common.http.cookies.CookiesManager;
import com.umeitime.common.http.interceptor.DecryptInterceptor;
import com.umeitime.common.http.interceptor.HttpHeaderInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
public class OkHttp3Utils {
    private static OkHttpClient mOkHttpClient;
//    private static File cacheDirectory = new File(AppContext.getInstance().getApplicationContext().getCacheDir().getAbsolutePath(), "SujianCache");
//    private static Cache cache = new Cache(cacheDirectory, 100 * 1024 * 1024);
    /*
     * 获取OkHttpClient对象
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        if (null == mOkHttpClient) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .cookieJar(new CookiesManager())
                    .addInterceptor(new DecryptInterceptor())
                    .addInterceptor(new HttpHeaderInterceptor())
//                    .cache(cache)
                    //.addNetworkInterceptor(new CookiesInterceptor(MyApplication.getInstance().getApplicationContext()))
                    //设置请求读写的超时时间
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
        return mOkHttpClient;
    }
}