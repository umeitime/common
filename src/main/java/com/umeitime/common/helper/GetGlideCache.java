package com.umeitime.common.helper;

import android.content.Context;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * Created by hujunwei on 17/7/25.
 */

public class GetGlideCache {
    public interface GlideResult {
        void onSuccess(String path);

        void onFail();
    }

    public static class GetImageCacheTask extends AsyncTask<Integer, Void, File> {
        private final Context mContext;
        private String pic;
        private GlideResult glideResult;

        public GetImageCacheTask(Context context, String pic, GlideResult glideResult) {
            mContext = context;
            this.pic = pic;
            this.glideResult = glideResult;
        }

        @Override
        protected File doInBackground(Integer... params) {
            try {
                return Glide.with(mContext)
                        .load(pic)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception ex) {
                glideResult.onFail();
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                glideResult.onFail();
                return;
            }
            //此path就是对应文件的缓存路径
            String path = result.getPath();
            glideResult.onSuccess(path);
        }
    }
}
