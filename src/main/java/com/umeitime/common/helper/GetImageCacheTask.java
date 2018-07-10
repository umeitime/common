package com.umeitime.common.helper;

import android.content.Context;
import android.os.AsyncTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import java.io.File;

public class GetImageCacheTask extends AsyncTask<String, Void, File> {
    private final Context context;
    private GetImageCacheResult getImageCacheResult;
    public GetImageCacheTask(Context context,GetImageCacheResult getImageCacheResult) {
        this.context = context;
        this.getImageCacheResult = getImageCacheResult;
    }

    @Override
    protected File doInBackground(String... params) {
        String imgUrl = params[0];
        try {
            return Glide.with(context)
                    .load(imgUrl)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(File result) {
        if (result == null) {
            getImageCacheResult.onSuccess("");
            return;
        }
        //此path就是对应文件的缓存路径
        String path = result.getPath();
        getImageCacheResult.onSuccess(path);
    }

    public interface GetImageCacheResult{
        void onSuccess(String path);
    }
}
