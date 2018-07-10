package com.umeitime.common.http.interceptor;

import com.umeitime.common.AppContext;
import com.umeitime.common.data.UserInfoDataManager;
import com.umeitime.common.http.DES;
import com.umeitime.common.tools.AppUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求公共头信息插入器
 * <p>
 * Created by June on 17/1/18.
 */
public class HttpHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .header("APP-Key", "APP-Secret222")
                .header("machine", AppUtils.getPhoneModel())
                .header("token", UserInfoDataManager.getUserInfo().token)
                .header("versioncode", AppUtils.getVersionCode(AppContext.getInstance().getApplicationContext()) + "")
                .header("Authorization", DES.encryptBasedDes(System.currentTimeMillis() + "umeitime20150715"))
                .header("AccessFrom", "Android")
                .build();
        return chain.proceed(request);
    }
}