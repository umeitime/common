package com.umeitime.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeitime.common.R;
import com.umeitime.common.tools.StringUtils;
import com.umeitime.common.tools.ToastUtil;
import com.umeitime.common.views.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hujunwei on 17/4/15.
 */

public abstract class BaseFragment extends Fragment {
    public Activity mContext;
    public View mRootView;
    public LoadingDialog loadingDialog;
    protected boolean isPrepared;
    protected boolean isDestroy;
    protected boolean isVisible;
    protected Toolbar toolbar;
    protected RelativeLayout appBar;
    protected TextView tvTitle;
    protected ImageView ivRight;
    protected AppCompatActivity mAppCompatActivity;
    protected Unbinder mUnbinder;
    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;

    protected abstract int getContentViewRes();

    protected abstract void initView();

    protected abstract void initEvent();

    protected abstract void initData();

    public void loadData(String keyWord){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isDestroy = false;
//        Cursor cursor = AppContext.getInstance().getApplicationContext().getContentResolver().query(DBContract.UserEntry.CONTENT_URI, null, null, null, null);
//        try {
//            cursor.moveToFirst();
//            uid = cursor.getInt(1);
//        }catch (Exception e){
//        } finally {
//            cursor.close();
//        }
        initPrepare();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewRes(), container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        initEvent();
        initData();
        return mRootView;
    }

    public void hideToolbar() {
        try {
            toolbar = mRootView.findViewById(R.id.toolbar);
            appBar = mRootView.findViewById(R.id.rlAppBar);
            appBar.setVisibility(View.GONE);
        } catch (Exception e) {

        }
    }
    public void initIvRight(int res){
        ivRight = mRootView.findViewById(R.id.ivRight);
        ivRight.setImageResource(res);
        ivRight.setVisibility(View.VISIBLE);
    }
    public Toolbar initToolbar() {
        toolbar = mRootView.findViewById(R.id.toolbar);
        appBar = mRootView.findViewById(R.id.rlAppBar);
        toolbar.setTitle("");
        if(canBack()){
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_gray_24dp);
        }
        tvTitle = mRootView.findViewById(R.id.tvTitle);
        mAppCompatActivity = (AppCompatActivity) mContext;
        mAppCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = mAppCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(canBack());
        }
        return toolbar;
    }

    protected boolean canBack() {
        return true;
    }

    public void showMsg(int resId) {
        showMsg(getString(resId));
    }

    public void showMsg(String msg) {
        ToastUtil.showToast(mContext, msg);
    }

    public LoadingDialog showProgressDialog(String message) {
        if (StringUtils.isEmpty(message)) message = getResources().getString(R.string.loading);
        loadingDialog = new LoadingDialog(mContext, message);
        loadingDialog.showDialog();
        return loadingDialog;
    }

    public void dismissProgressDialog() {
        if (loadingDialog != null && loadingDialog.loadingDialog.isShowing()) {
            loadingDialog.dismissDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {

            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public void onFirstUserVisible() {
    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {
    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible() {
    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {
    }

    @Override
    public void onDestroyView() {
        isDestroy = true;
        if (this.mUnbinder != null) {
            this.mUnbinder.unbind();
        }
        super.onDestroyView();
    }
}
