package com.umeitime.common.http;

import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hujunwei on 16/6/27.
 */
public class MyStringCallback extends StringCallback {
    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException {
        String content = response.body().string();
        return content;
    }
    @Override
    public void onBefore(Request request, int id) {
        super.onBefore(request,id);
    }

    @Override
    public void onAfter(int id) {
        super.onAfter(id);
    }

    @Override
    public void onError(Call call, Exception e,int id) {
//        e.printStackTrace();
    }

    @Override
    public void onResponse(String response,int id) {

    }
}
