package com.umeitime.common.http.interceptor.factory;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.umeitime.common.tools.LogUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/**
 * Created by zhang on 2016/5/31.
 * <p>
 * 自定义请求RequestBody
 */
public class JsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private final Charset UTF_8 = Charset.defaultCharset();
    /**
     * 构造器
     */

    public JsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        //加密
//        APIBodyData data = new APIBodyData();
        LogUtils.i("Http", "request中传递的json数据：" + value.toString());
//        data.setData(DES.encryptBasedDes(value.toString()));
//        String postBody = gson.toJson(data); //对象转化成json
//        LogUtils.i("Http", "转化后的数据：" + postBody);
//        return RequestBody.create(MEDIA_TYPE, postBody);
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = gson.newJsonWriter(writer);
        adapter.write(jsonWriter, value);
        jsonWriter.close();
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
    }

}