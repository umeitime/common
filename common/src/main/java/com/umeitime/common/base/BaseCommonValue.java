package com.umeitime.common.base;

/**
 * Created by hujunwei on 17/7/20.
 */

public class BaseCommonValue {
    public static String USER_TOKEN = "";
    public static final int QINIU_EXPIRE_TIME = 3600;//单位秒
    //errorCode
    public static final int TOKEN_EXPRIED = -101;
    public static final int GSON_FORMAT_ERROR = -102;
    public static final int WEB_RESP_CODE_SUCCESS = 200;
    public static final String QINIU_URL = "http://www.umeitime.com/";
    //指定宽度和高度居中裁切
    public static String getImageUrl(String url, int width, int height) {
        url = url+"?imageMogr2/auto-orient/thumbnail/"+width+"x/gravity/center/crop/"+width+"x"+height+"/format/webp|imageslim";
        return url;
    }
    //根据图片宽度缩放
    public static String getImageUrlByWidth(String url, int width) {
        url = url+"?imageMogr2/auto-orient/thumbnail/"+width+"x/format/webp|imageslim";
        return url;
    }
    //gif 缩放最后转化为静态图
    public static String getGifImageUrl(String url, int width, int height) {
        url = url+"?imageMogr2/auto-orient/thumbnail/"+width+"x/gravity/center/crop/"+width+"x"+height+"/format/jpg|imageslim";
        return url;
    }
    //指定宽高缩放 返回webp
    public static String getImageUrl2(String url, int width, int height) {
        return url + "?imageMogr2/auto-orient/thumbnail/"+width+"x/gravity/north/crop/"+width+"x"+height+"/format/webp|imageslim";
    }
    //不缩放 转换为webp
    public static String getSlimPic(String url) {
        if(url.contains("img.mmdstar.com"))
            return url + "?imageMogr2/thumbnail/1000x/format/webp|imageslim";
        else
            return url;
    }
    //不缩放 压缩体积
    public static String getGifSlimPic(String url) {
        if(url.startsWith(QINIU_URL))
            return url + "?imageslim";
        else
            return url;
    }
    //获取模糊后的图片
    public static String getBlurImageUrl(String url) {
        return url + "?imageMogr/auto-orient/format/jpg/blur/50x30";
    }
}
