package com.umeitime.common.qiniu;


import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.umeitime.common.base.BaseCommonValue;
import com.umeitime.common.tools.FileUtils;
import com.umeitime.common.tools.MD5Utils;
import com.umeitime.common.tools.StringUtils;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JW on 2015/12/24.
 */
public class ImageUploader {
    public static ImageUploader imageUploader;
    public int totalSize;
    public int successSize;
    private String mToken;
    private UploadListener uploadListener;
    private UploadManager uploadManager;
    private List<String> imgUrls = new ArrayList<>();
    public ImageUploader(String token) {
        mToken = token;
        uploadManager = new UploadManager();
    }

    public ImageUploader getInstance(String token) {
        if (imageUploader == null) {
            imageUploader = new ImageUploader(token);
        }
        return imageUploader;
    }

    public void setUploadListener(UploadListener uploadListener) {
        this.uploadListener = uploadListener;
    }

    public void uploadPic(List<String> paths, String keyPre) {
        if (uploadListener == null) return;
        if(imgUrls.size()>0){
            imgUrls.clear();
        }
        totalSize = paths.size();
        for(int c=0;c<paths.size();c++){
            imgUrls.add("");
        }
        for (int i = 0; i < paths.size(); i++) {
            final int index = i;
            String path = paths.get(index);
            if (new File(path).exists()) {
                String key = keyPre+"/"+ MD5Utils.encodeMD5(new File(path));
                if (FileUtils.getFileSize(path) > 500) {
                    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                    Tiny.getInstance().source(path).asFile().withOptions(options).compress(new FileCallback() {
                        @Override
                        public void callback(boolean isSuccess, String outfile) {
                            String imgPath = path;
                            if (isSuccess) {
                                imgPath = outfile;
                            }
                            uploadPic2(imgPath, key, index);
                        }
                    });
                } else {
                    uploadPic2(path, key, index);
                }
            } else if (path.startsWith("http")) {
                successSize+=1;
                imgUrls.set(i, path);
                onResult();
            }
        }
    }

    private void uploadPic2(String path, String key, int index){
        uploadManager.put(path, key, mToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (response == null) {
                            uploadListener.onFailure("response=null");
                        } else {
                            try {
                                String imgUrl = "";
                                if(StringUtils.isBlank(key)) {
                                    if(response.has("hash")) {
                                        imgUrl = BaseCommonValue.QINIU_URL+"/"+new JSONObject(response.toString()).getString("hash");
                                        imgUrls.set(index, imgUrl);
                                    }else{
                                        uploadListener.onFailure("response no hash");
                                    }
                                }else{
                                    imgUrls.set(index, BaseCommonValue.QINIU_URL+"/"+key);
                                }
                                successSize+=1;
                                onResult();
                            } catch (Exception e) {
                                uploadListener.onFailure(e.toString());
                                onResult();
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

    public void onResult(){
        if(successSize==totalSize){
            uploadListener.onSuccess(imgUrls);
            Tiny.getInstance().clearCompressDirectory();
        }
    }
    public interface UploadListener {
        void onSuccess(List<String> imgs);

        void onFailure(String msg);

        void onPercent(double percent);
    }
}
