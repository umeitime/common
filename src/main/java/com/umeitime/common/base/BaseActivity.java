package com.umeitime.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.umeitime.common.R;
import com.umeitime.common.model.LocationBean;
import com.umeitime.common.tools.SPUtil;
import com.umeitime.common.tools.StringUtils;
import com.umeitime.common.tools.SystemBarHelper;
import com.umeitime.common.tools.ToastUtil;
import com.umeitime.common.views.LoadingDialog;

import butterknife.ButterKnife;

/**
 * Created by hujunwei on 17/6/10.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected Toolbar mToolbar;
    protected TextView tvTitle;
    protected View line;
    protected RelativeLayout appBar;
    protected String TAG = this.getClass().getSimpleName();
    protected boolean isDestroy;
    protected boolean isVisible;
    protected boolean isFirst;
    protected LocationBean location;
    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                String aoiName = loc.getAoiName();
                double latitude = loc.getLatitude();
                double longitude = loc.getLongitude();
                location.adress = aoiName;
                location.latitude = latitude;
                location.longitude = longitude;
                if (!isFirst & location.latitude > 0) {
                    isFirst = true;
                    SPUtil.put(mContext, "LOCATION", new Gson().toJson(location));
                    onLocationSuccess();
                }
                Log.e("location", "aoiName" + new Gson().toJson(loc));
            } else {
                Log.e("location", "null");
            }
        }
    };
    private LoadingDialog loadingDialog;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewRes());
        ButterKnife.bind(this);
        mContext = this;
        initView();
        initEvent();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    protected abstract int getContentViewRes();

    protected abstract void initView();

    protected abstract void initEvent();

    protected abstract void initData();

    protected void showMsg(String msg) {
        ToastUtil.showToast(mContext, msg);
    }

    protected void showMsg(int res) {
        ToastUtil.showToast(mContext, getString(res));
    }

    protected boolean canBack() {
        return true;
    }

    protected void initToolbar(String title) {
        appBar = findViewById(R.id.rlAppBar);
        mToolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.tvTitle);
        line = findViewById(R.id.line);
        tvTitle.setText(title);
        mToolbar.setTitle("");
        ActionBar actionBar = getSupportActionBar();
        mToolbar.setContentInsetStartWithNavigation(0);
        if (canBack()) {
            mToolbar.setNavigationIcon(R.drawable.ic_toolbar_back_gray_24dp);
        }
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(canBack());
        }
        setSupportActionBar(mToolbar);
        SystemBarHelper.immersiveStatusBar(this);
        SystemBarHelper.setStatusBarDarkMode(this);
        SystemBarHelper.setHeightAndPadding(mContext, appBar);
    }

    protected void initTvRight(String title){
        TextView tvRight = findViewById(R.id.tvRight);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(title);
    }

    protected void initToolbarNoAppBar(String title) {
        mToolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.tvTitle);
        line = findViewById(R.id.line);
        tvTitle.setText(title);
        mToolbar.setTitle("");
        ActionBar actionBar = getSupportActionBar();
        mToolbar.setContentInsetStartWithNavigation(0);
        if (canBack()) {
            mToolbar.setNavigationIcon(R.drawable.ic_toolbar_back_white_24dp);
        }
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(canBack());
        }
        setSupportActionBar(mToolbar);
        SystemBarHelper.immersiveStatusBar(this);
        SystemBarHelper.setStatusBarLightMode(this);
        SystemBarHelper.setHeightAndPadding(mContext, mToolbar);
    }

    public void showProgressDialog(String message) {
        if (StringUtils.isEmpty(message)) message = getResources().getString(R.string.loading);
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext, message);
        }
        if (loadingDialog.loadingDialog != null && loadingDialog.loadingDialog.isShowing()) return;
        loadingDialog.showDialog();
    }

    public void dismissProgressDialog() {
        if (loadingDialog != null && loadingDialog.loadingDialog.isShowing()) {
            loadingDialog.dismissDialog();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showLoading(String charSequence) {
        showProgressDialog(charSequence);
    }

    public void hideLoading() {
        dismissProgressDialog();
    }

    @Override
    protected void onDestroy() {
        isDestroy = true;
        super.onDestroy();
        destroyLocation();
    }

    public void onLocationSuccess() {

    }

    /**
     * 定位参数配置
     */
    protected void initLocation() {
        location = new LocationBean();
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    protected AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
//        mOption.setInterval(5*60000);//可选，设置定位间隔。默认为2秒
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，
        // 启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mOption.setOnceLocationLatest(true);
        mOption.setWifiActiveScan(true);
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        return mOption;
    }

    /**
     * 开始定位
     */
    protected void startLocation() {
        //根据控件的选择，重新设置定位参数
        // 设置定位参数
        if (locationClient != null) {
            locationClient.setLocationOption(locationOption);
            // 启动定位
            locationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    protected void stopLocation() {
        // 停止定位
        if (locationClient != null)
            locationClient.stopLocation();
    }

    /**
     * 销毁定位
     */
    protected void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
}
