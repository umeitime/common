package com.umeitime.common.http.interceptor;


import com.umeitime.common.http.DES;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
/**
 * Created by Administrator on 2016/8/1.
 */
public class DecryptInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        ResponseBody body = response.body();
        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE);
        String content = new String(source.buffer().clone().readString(Charset.forName("UTF-8")));
        String data;
        try {
            data = DES.decryptBasedDes(content);
        }catch (Exception e){
            data = content;
        }
        source.buffer().clear();
        source.buffer().write(data.getBytes());
        response.cacheResponse();
        return response;
    }
}