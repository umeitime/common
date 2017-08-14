package com.umeitime.common.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.tencent.tauth.Tencent;

import java.io.File;


/**
 * Created by hujunwei on 17/1/13.
 */

public class IntentUtils {
    public static final int requestCode_photo = 200;
    //社交APP包名
    public static final String SINA_PKG = "com.sina.weibo";
    public static final String QQ_PKG = "com.tencent.mobileqq";
    public static final String XIMA_PKG = "com.ximalaya.ting.android";
    public static final String MUSIC163_PKG = "com.netease.cloudmusic";
    public static final String DOUBAN_PKG = "com.douban.frodo";
    //网页拼接前缀
    public static final String ximaPageUrl = "http://www.ximalaya.com/";
    public static final String ximaUrl = "http://www.ximalaya.com/sound/";
    public static final String doubanUrl = "http://www.douban.com/note/";
    public static final String doubanPageUrl = "https://www.douban.com/people/";
    public static final String musicPageUrl = "http://music.163.com/m/user/home?id=";
    public static final String musicAlbumUrl = "http://music.163.com/album/";
    public static final String weiboPageUrl = "http://m.weibo.com/u/";
    public static final String weiboUrl = "http://m.weibo.cn/status/";

    /**
     * 跳转到app详情界面
     *
     * @param appPkg    App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void launchAppDetail(Context context, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg))
                return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toQQ(Activity context, String QQ_APP_ID, String qq){
        Tencent mTencent = Tencent.createInstance(QQ_APP_ID, context);
        mTencent.startWPAConversation(context, qq, "");
    }

    public static boolean openWebUrl(Context mContext, String url) {
        if (url.startsWith(musicAlbumUrl) && AppUtils.isAppInstalled(mContext, MUSIC163_PKG)) {
            url = "orpheus://album/" + url.replace(musicAlbumUrl, "");
            openIntent(mContext, url);
            return true;
        } else if (url.startsWith(musicPageUrl) && AppUtils.isAppInstalled(mContext, MUSIC163_PKG)) {
            url = "orpheus://artist/" + url.replace(musicPageUrl, "");
            openIntent(mContext, url);
            return true;
        } else if (url.contains(ximaUrl) && AppUtils.isAppInstalled(mContext, XIMA_PKG)) {
            url = "iting://open?msg_type=11&track_id=" + url.replace(ximaUrl, "");
            openIntent(mContext, url);
            return true;
        } else if (url.contains(ximaPageUrl) && AppUtils.isAppInstalled(mContext, XIMA_PKG)) {
            url = "iting://open?msg_type=12&uid=" + url.replace(ximaPageUrl, "");
            openIntent(mContext, url);
            return true;
        } else if ((url.contains("music.163.com/playlist/") || url.contains("music.163.com/song/")) && AppUtils.isAppInstalled(mContext, MUSIC163_PKG)) {
            url = "orpheus://" + url.split("\\?")[0].replace("http://music.163.com/", "");
            openIntent(mContext, url);
            return true;
        } else if ((url.contains("music.163.com/#/song?id") || url.contains("music.163.com/#/playlist?id")) && AppUtils.isAppInstalled(mContext, MUSIC163_PKG)) {
            String urls[] = url.split("\\?");
            String pre = urls[0].replace("http://music.163.com/#/", "");
            String ends[] = urls[1].split("&");
            String id = ends[0].replace("id=", "");
            url = "orpheus://" + pre + "/" + id;
            openIntent(mContext, url);
            return true;
        } else if (url.contains("douban.com/doubanapp/dispatch") && AppUtils.isAppInstalled(mContext, "")) {
            url = "douban://douban.com/" + (url.split("\\?")[1].replace("uri=/", ""));
            openIntent(mContext, url);
            return true;
        } else if (url.contains("douban.com") && AppUtils.isAppInstalled(mContext, DOUBAN_PKG)) {
            url = url.replace("http:", "douban:");
            openIntent(mContext, url);
            return true;
        } else if (url.contains("jianshu.com/p") && AppUtils.isAppInstalled(mContext, DOUBAN_PKG)) {
            String urls[] = url.split("/");
            url = "jianshu://notes/" + urls[urls.length - 1];
            openIntent(mContext, url);
            return true;
        } else if (url.contains(weiboUrl) && AppUtils.isAppInstalled(mContext, SINA_PKG)) {
            url = "sinaweibo://detail?mblogid=" + url.replace(weiboUrl, "");
            openIntent(mContext, url);
            return true;
        } else if (url.contains(weiboPageUrl) && AppUtils.isAppInstalled(mContext, SINA_PKG)) {
            url = "sinaweibo://userinfo?uid=" + url.replace(weiboPageUrl, "");
            openIntent(mContext, url);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 启动Url
     */
    public static void openIntent(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        context.startActivity(intent);
    }
    //打图库
    public static void openGallery(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        activity.startActivityForResult(intent, requestCode_photo);
    }
    /**
     * 加入QQ群
     *
     * @param context     上下文
     * @param qqGroupName 加群的代码 http://qun.qq.com/join.html 获取Android代码
     */
    public static boolean joinQQGroup(Context context, String qqGroupName) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=" +
                "http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom" +
                "%3Dapp%26p%3Dandroid%26k%3D" + qqGroupName));
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public static void openPic(Context context, File picFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(picFile), "image/*");
        context.startActivity(intent);
    }

    public static void shareAll(Context context,String msg){
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, msg);
        context.startActivity(Intent.createChooser(textIntent, "分享"));
    }

    public static void toEmail(Context mContext, String email){
        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:"+email));
        data.putExtra(Intent.EXTRA_SUBJECT, "反馈建议");
        data.putExtra(Intent.EXTRA_TEXT, "");
        mContext.startActivity(data);
    }
    /**
     * Constructs an intent for image cropping. 调用图片剪辑程序
     */
    public static Intent getCropImageIntent(Uri photoUri, int outWith, int outHeight, File outPath) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");    // crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", outWith);   // 这两项为裁剪框的比例.
        intent.putExtra("aspectY", outHeight);
        intent.putExtra("output", Uri.fromFile(outPath));
        intent.putExtra("outputFormat", "JPEG");//返回格式
        return intent;
    }
}
