package com.umeitime.common.http.callback;

import com.umeitime.common.AppContext;
import com.umeitime.common.R;
import com.umeitime.common.base.BaseCommonValue;
import com.umeitime.common.base.BaseEvent;
import com.umeitime.common.http.ApiException;
import com.umeitime.common.http.entry.JsonResponse;
import com.umeitime.common.tools.NetWorkUtils;

import de.greenrobot.event.EventBus;
import rx.Subscriber;
public abstract class ApiCallback<T> extends Subscriber<JsonResponse<T>> {

    public abstract void onFailure(String msg);

    public void onFailure(T data, String msg){

    }

    public abstract void onFinish();

    public abstract void onSuccess(T data);

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            if (exception.isTokenExpried()) {
                EventBus.getDefault().post(new BaseEvent.TokenExpireEvent());
            }else if(exception.mErrorCode== BaseCommonValue.GSON_FORMAT_ERROR){
                onFailure("gson format error!");
            }else{
                if(!NetWorkUtils.checkNetwork(AppContext.getInstance().getApplicationContext())) {
                    onFailure(AppContext.getInstance().getApplicationContext().getString(R.string.nonet));
                }else{
                    onFailure(e.getMessage());
                }
            }
        }else{
            if(!NetWorkUtils.checkNetwork(AppContext.getInstance().getApplicationContext())) {
                onFailure(AppContext.getInstance().getApplicationContext().getString(R.string.nonet));
            }else{
                onFailure(e.toString());
            }
        }
        onFinish();
    }

    @Override
    public void onNext(JsonResponse response) {
        if (response.code == 200) {
            onSuccess((T) response.data);
        } else if (response.code == 201) {
            onFailure((T) response.data, response.msg);
        }
        onFinish();
    }

    @Override
    public void onCompleted() {
        onFinish();
    }
}
