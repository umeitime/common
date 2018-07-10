package com.umeitime.common.base;

import android.content.Context;

import com.umeitime.common.AppContext;
import com.umeitime.common.data.UserInfoDataManager;
import com.umeitime.common.tools.ToastUtil;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
public class BasePresenter<V> {
    public V mvpView;
    protected CompositeSubscription mCompositeSubscription;
    protected Context mContext;
    public void attachView(V mvpView) {
        this.mvpView = mvpView;
        mContext = AppContext.getInstance().getApplicationContext();
    }
    public void showMsg(String msg){
        ToastUtil.showToast(AppContext.getInstance().getApplicationContext(), msg);
    }
    public void detachView() {
        this.mvpView = null;
        onUnsubscribe();
    }

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
