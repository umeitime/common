package com.umeitime.common.http.interceptor.factory;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.umeitime.common.http.DES;
import com.umeitime.common.http.entry.JsonResponse;
import com.umeitime.common.tools.LogUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson mGson;//gson对象
    private final TypeAdapter<T> adapter;
    /**
     * 构造器
     */
    public JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.mGson = gson;
        this.adapter = adapter;
    }
    /**
     * 转换
     *
     * @param responseBody
     * @return
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        String response = responseBody.string();
        String result = DES.decryptBasedDes(response);//解密
        LogUtils.i("Http", "解密的服务器数据：" + result);
        JsonResponse jsonResponse = mGson.fromJson(result, JsonResponse.class);
        return (T) jsonResponse;
    }
}