package com.umeitime.common.helper;

import android.content.Context;

import com.umeitime.common.base.BaseCommonValue;
import com.umeitime.common.qiniu.ImageUploader;
import com.umeitime.common.qiniu.QiniuPresenter;
import com.umeitime.common.qiniu.QiniuView;
import com.umeitime.common.tools.SPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hujunwei on 17/7/11.
 */

public class QiniuUpload implements QiniuView {
    public static final String qiniuKey = "qiniuToken";
    public static final String qiniuKeyTime = "qiniuTokenTime";
    public QiniuPresenter qiniuPresenter;
    public Context mContext;
    public String keyPre;
    public List<String> paths;
    public ImageUploader.UploadListener mUploadListener;

    public QiniuUpload(Context context) {
        mContext = context;
        qiniuPresenter = new QiniuPresenter(this);
    }

    public void uploadPic(String path, String key, ImageUploader.UploadListener uploadListener) {
        paths = new ArrayList<>();
        paths.add(path);
        uploadPics(paths, key, uploadListener);
    }

    public void uploadPics(List<String> paths, String keyPre, ImageUploader.UploadListener uploadListener) {
        this.paths = paths;
        this.keyPre = keyPre;
        mUploadListener = uploadListener;
        String token = (String) SPUtil.get(mContext, qiniuKey, "");
        long tokenTime = (long) SPUtil.get(mContext, qiniuKeyTime, 0l);
        if (token.equals("") || (System.currentTimeMillis() - tokenTime) / 1000 > BaseCommonValue.QINIU_EXPIRE_TIME) {
            qiniuPresenter.getQnToken();
        } else {
            uploadByToken(token);
        }
    }

    public void uploadByToken(String qiniuToken) {
        ImageUploader imageUploader = new ImageUploader(qiniuToken);
        imageUploader.setUploadListener(mUploadListener);
        imageUploader.uploadPic(paths, keyPre);
    }
    @Override
    public void showLoading(String msg) {

    }
    @Override
    public void hideLoading() {

    }
    @Override
    public void getDataFail(String msg) {
        mUploadListener.onFailure(msg);
    }

    @Override
    public void showToken(String token) {
        SPUtil.put(mContext,qiniuKey,token);
        SPUtil.put(mContext,qiniuKeyTime,System.currentTimeMillis());
        uploadByToken(token);
    }
}
