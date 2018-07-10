package com.umeitime.common.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by hujunwei on 17/6/7.
 */
public class DownloadDir {
    public static String SD = Environment.getExternalStorageDirectory().getPath();
    public static String saveDir = SD + "/umeitime/save/";
    public static String cropDir = SD + "/umeitime/crop/";
    public static String savePic = SD + "/umeitime/save/";
    public static String apkDir = SD + "/umeitime/apk/";
    public static String logoDir = SD + "/umeitime/logo/";
    public static String download = SD + "/Download/umeitime/";
    public static String getSavePicDir() {
        File cacheDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        if (cacheDir != null) {
            File result = new File(cacheDir, "优美时光");
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                // File wasn't able to create a directory, or the result exists but not a directory
                File savePicDir = new File(savePic);
                if(!savePicDir.mkdirs() && (!savePicDir.exists() || !savePicDir.isDirectory())){
                    return null;
                }
                return savePicDir.getAbsolutePath();
            }
            return result.getAbsolutePath();
        }else{
            File savePicDir = new File(savePic);
            if(!savePicDir.mkdirs() && (!savePicDir.exists() || !savePicDir.isDirectory())){
                return null;
            }
            return savePicDir.getAbsolutePath();
        }
    }
}
