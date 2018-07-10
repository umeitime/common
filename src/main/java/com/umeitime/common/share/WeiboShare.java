package com.umeitime.common.share;

import android.app.Activity;
import android.graphics.Bitmap;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.umeitime.common.base.BaseCommonValue;
import com.umeitime.common.tools.BitmapUtils;
import com.umeitime.common.tools.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WeiboShare {
    // 第三方APP和微信通信的openApi接口
    public static IWeiboShareAPI mWeiboShareAPI;//微博分享
    private static Activity mContext;
    private int share_to = 0;
    private int share_type = 0;
    private String url = "";
    private String audioUrl = "";
    private String title = "";
    private String filePath = "";
    private String description = "";

    public static WeiboShare regToWeibo(Activity context, String WEIBO_APP_KEY) {
        mContext = context;
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, WEIBO_APP_KEY);
        mWeiboShareAPI.registerApp();
        return new WeiboShare();
    }

    public WeiboShare setWhere(int shareTo) {
        share_to = shareTo;
        return this;
    }

    /**
     * 分享的类型
     *
     * @param type
     * @return
     */
    public WeiboShare setType(int type) {
        share_type = type;
        return this;
    }

    /**
     * 分享的网页Url
     *
     * @param url
     * @return
     */
    public WeiboShare addUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 分享的音频Url
     *
     * @param audioUrl
     * @return
     */
    public WeiboShare addAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
        return this;
    }

    /**
     * 分享的标题
     *
     * @param title
     * @return
     */
    public WeiboShare addTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 描述
     *
     * @param description
     * @return
     */
    public WeiboShare addDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * 分享的图片path
     *
     * @param filePath
     * @return
     */
    public WeiboShare addImagePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void share() {
        if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
            int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
            if (supportApi >= 10351) {
                try {
                    sendMultiMessage();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                sendSingleMessage();
            }
        } else {
            shareImageByPkg();
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。 注意：当
     * {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     */
    public void sendMultiMessage() {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        String content = "";
        if (share_type == 1) {//分享图片
            content = "优美时光,美图分享。";
        } else {//分享文章
            content = "「" + title + "」" + ">>>全文阅读" + url;
        }
        textObject.text = content + BaseCommonValue.UMEI;
        weiboMessage.textObject = textObject;
        if (StringUtils.isNotBlank(filePath)) {
            Bitmap thumb = BitmapUtils.getSDCardImg(filePath);
            if (thumb != null) {
                ImageObject imageObject = new ImageObject();
                imageObject.setImageObject(thumb);
                weiboMessage.imageObject = imageObject;
            }
            thumb.recycle();
        }
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        mWeiboShareAPI.sendRequest(mContext, request);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()}
     * < 10351 时，只支持分享单条消息，即 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     * flag 0 图文 1纯图
     */
    public void sendSingleMessage() {
        WeiboMessage weiboMessage = new WeiboMessage();
        if (share_type == 1) {
            ImageObject imageObject = new ImageObject();
            imageObject.imagePath = filePath;
            weiboMessage.mediaObject = imageObject;
        } else {
            TextObject textObject = new TextObject();
            textObject.text = description;
            weiboMessage.mediaObject = textObject;
        }
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;
        mWeiboShareAPI.sendRequest(mContext, request);
    }
    //自定义分享到微博 解决不支持sdk的bug
    public void shareImageByPkg() {
        List<File> images = new ArrayList<>();
        String content = "";
        if (share_type == 1) {//分享图片
            content = "优美时光,美图分享。";
            images.add(new File(filePath));
        } else {//分享文章
            content = "「" + title + "」" + ">>>全文阅读" + url;
        }
        ShareUtils.shareSinaWeibo(mContext, content + BaseCommonValue.UMEI, images);
    }

}