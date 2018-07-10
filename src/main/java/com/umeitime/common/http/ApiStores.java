package com.umeitime.common.http;

import com.umeitime.common.http.entry.JsonResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiStores {
    @FormUrlEncoded
    @POST("getQnToken")
    Observable<JsonResponse<String>> getQnToken(@Field("bucket") String bucket);
}
