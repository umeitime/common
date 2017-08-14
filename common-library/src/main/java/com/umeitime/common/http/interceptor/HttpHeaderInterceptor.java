package com.umeitime.common.http.interceptor;

import com.umeitime.common.AppContext;
import com.umeitime.common.base.BaseCommonValue;
import com.umeitime.common.tools.AppUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
* 网络请求公共头信息插入器
*
* Created by June on 17/1/18.
*/
public class HttpHeaderInterceptor implements Interceptor {
  @Override
  public Response intercept(Chain chain) throws IOException {
      Request original = chain.request();
      Request request = original.newBuilder()
              .header("User-Agent", "Sujian_"+ AppUtils.getVersionCode(AppContext.getInstance().getApplicationContext()))
              .header("Token", BaseCommonValue.USER_TOKEN)
              .build();
      return chain.proceed(request);
  }
}