package com.umeitime.common.http;
import com.umeitime.common.base.BaseCommonValue;
import com.umeitime.common.base.BasePresenter;

/**
 * Created by hujunwei on 2018/3/27.
 */

public class AppPresenter<V> extends BasePresenter<V>{
    protected ApiStores apiStores;
    protected int uid;
    @Override
    public void attachView(V mvpView) {
        super.attachView(mvpView);
        apiStores = AppClient.retrofit(BaseCommonValue.API_SERVER_URL).create(ApiStores.class);
    }
}
