package com.umeitime.common.http.interceptor.factory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.umeitime.common.base.BaseCommonValue;
import com.umeitime.common.http.ApiException;
import com.umeitime.common.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Charset UTF_8 = Charset.defaultCharset();
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        HttpStatus httpStatus = gson.fromJson(response, HttpStatus.class);
        if (httpStatus.isCodeInvalid()) {
            value.close();
            throw new ApiException(httpStatus.getCode(), httpStatus.getMessage());
        }

        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);

        try {
            return adapter.read(jsonReader);
        }catch (Exception e){
            throw new ApiException(BaseCommonValue.GSON_FORMAT_ERROR, e.toString());
        } finally {
            value.close();
        }
    }
}
