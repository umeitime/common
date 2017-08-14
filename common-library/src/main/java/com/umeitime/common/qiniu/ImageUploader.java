package com.umeitime.common.qiniu;

import android.content.Context;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.umeitime.common.tools.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by JW on 2015/12/24.
 */
public class ImageUploader {
    private String mPath;
    private String mKey;
    private String mToken;
    private Context mContext;
    private UploadListener uploadListener;
    private UploadManager uploadManager;
    public static ImageUploader imageUploader;

    public ImageUploader getInstance(Context context, String token) {
        if (imageUploader == null) {
            imageUploader = new ImageUploader(context, token);
        }
        return imageUploader;
    }

    public ImageUploader(Context context, String token) {
        mContext = context;
        mToken = token;
        uploadManager = new UploadManager();
    }

    public interface UploadListener {
        void onSuccess(String imageUrl, String imgPath);

        void onFailure(String imgPath);

        void onPercent(double percent);
    }

    public void setUploadListener(UploadListener uploadListener) {
        this.uploadListener = uploadListener;
    }


    public void uploadPic(final String path, String key) {
        if (uploadListener == null) return;
        uploadManager.put(path, key, mToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (response == null) {
                            uploadListener.onFailure(path);
                        } else {
                            try {
                                String imgUrl = "";
                                if(StringUtils.isBlank(key)) {
                                    if(response.has("hash")) {
                                        imgUrl = new JSONObject(response.toString()).getString("hash");
                                        uploadListener.onSuccess(imgUrl, path);
                                    }else{
                                        uploadListener.onFailure(path);
                                    }
                                }else{
                                    uploadListener.onSuccess(key, path);
                                }
                            } catch (JSONException e) {
                                uploadListener.onFailure(path);
                            }
                        }
                    }
                }, new UploadOptions(null, null, false,
                        new UpProgressHandler() {
                            public void progress(String key, double percent) {
                                uploadListener.onPercent(percent);
                            }
                        }, null));
    }
}
