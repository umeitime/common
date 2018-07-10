package com.umeitime.common.base;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;

import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeitime.common.R;
import com.umeitime.common.tools.IntentUtils;
import com.umeitime.common.tools.NetWorkUtils;
import com.umeitime.common.tools.StringUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by hujunwei on 17/7/28.
 */

public class BrowserActivity extends BaseActivity {
    public static final int FILECHOOSER_RESULTCODE = 200;
    LinearLayout llLoading;
    LinearLayout llNoNet;
    WebView mWebView;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private List<String> titles;
    private String mTitle;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        initToolbar("正在加载中...");
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_brower;
    }

    @Override
    protected void initView() {
        mWebView = findViewById(R.id.webView);
        llNoNet = findViewById(R.id.nonet_view);
        llLoading = findViewById(R.id.loading_view);
        titles = new ArrayList<>();
        initWebView();
    }
    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    public void initWebView() {
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        mWebView.setWebChromeClient(new MyWebViewChromeClient()); // 处理解析，渲染网页等浏览器做的事情
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                if(url.startsWith("https") || url.startsWith("http")){
                    view.loadUrl(url);
                }else{
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // TODO: handle exception
                    }
                }
                return true;
            }
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                if(!NetWorkUtils.checkNetwork(mContext)){
                    mWebView.setVisibility(View.GONE);
                    llLoading.setVisibility(View.GONE);
                    llNoNet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "onPageFinished ");
                super.onPageFinished(view, url);
                llLoading.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            url = bundle.getString("url","");
            mWebView.loadUrl(url);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (mUploadMessage != null) {
                Uri result = data == null || resultCode != RESULT_OK ? null
                        : data.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            } else if (mUploadCallbackAboveL != null) {
                Uri[] results = null;
                if (data == null) {
                } else {
                    String dataString = data.getDataString();
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        results = new Uri[clipData.getItemCount()];
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            results[i] = item.getUri();
                        }
                    }
                    if (dataString != null)
                        results = new Uri[]{Uri.parse(dataString)};
                }
                mUploadCallbackAboveL.onReceiveValue(results);
                mUploadCallbackAboveL = null;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断是否可以返回操作
        if (mWebView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            mWebView.goBack();
            if (titles.size() > 1) {
                titles.remove(titles.size() - 1);
                if(tvTitle!=null)
                    tvTitle.setText(titles.get(titles.size() - 1));
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public String getShareTitle() {
        String title = titles.get(titles.size() - 1);
        if (title.equals("喜马拉雅FM")) {
            if (titles.size() - 2 >= 0) {
                title = titles.get(titles.size() - 2);
            }
        }
        return title;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_browser) {
            IntentUtils.openWebUrl(mContext, mWebView.getUrl());

        } else if (i == R.id.action_share) {
            IntentUtils.shareAll(mContext, getShareTitle() + " " + mWebView.getUrl());

        } else if (i == R.id.action_copy) {
            StringUtils.copy(mWebView.getUrl(), mContext);
            showMsg("复制成功");

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
        }
        super.onDestroy();
    }

    public class MyWebViewChromeClient extends WebChromeClient {
        // 监听网页加载进度
        public void onProgressChanged(WebView view, int newProgress) {
            // 设置进度条进度
        }
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (StringUtils.isNotBlank(url)&&StringUtils.isNotBlank(title)) {
                mTitle = title;
                if(tvTitle==null)return;
                tvTitle.setText(mTitle);
                titles.add(title);
                llLoading.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
            }
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "选择照片"),
                    FILECHOOSER_RESULTCODE);
        }

        // For Android  > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "选择照片"), FILECHOOSER_RESULTCODE);
        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(
                    Intent.createChooser(i, "选择照片"),
                    FILECHOOSER_RESULTCODE);
            return true;
        }
    }

    // 内部类
    class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String downUrl, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(downUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
