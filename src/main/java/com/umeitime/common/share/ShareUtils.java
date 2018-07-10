package com.umeitime.common.share;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.umeitime.common.tools.IntentUtils;
import com.umeitime.common.tools.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hujunwei on 2018/6/10.
 */

public class ShareUtils {
    //微信朋友圈 图+文
    public static void shareWxPerson(Context context, String shareContent, List<File> images) {
        ComponentName comp = new ComponentName(IntentUtils.WECHAT_PKG, "com.tencent.mm.ui.tools.ShareImgUI");
        sharePicAll(context, comp, shareContent, images);
    }

    //微信朋友圈 图+文
    public static void shareWxFriends(Context context, String shareContent, List<File> images) {
        ComponentName comp = new ComponentName(IntentUtils.WECHAT_PKG, "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        sharePicAll(context, comp, shareContent, images);
    }
    //新浪微博 图+文
    public static void shareSinaWeibo(Context context, String shareContent, List<File> images) {
        ComponentName comp = new ComponentName(IntentUtils.SINA_PKG, "com.sina.weibo.EditActivity");
        sharePicAll(context, comp, shareContent, images);
    }
    //利用包名分享
    public static void shareAll(Context context, String packageName, String msgText, List<File> images) {
        ArrayList<Uri> picUriList = new ArrayList<>();
        for (File picFile : images) {
            Uri picUri = Uri.fromFile(picFile);
            picUriList.add(picUri);
        }
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, picUriList);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.putExtra("Kdescription", msgText);
        intent.putExtra(Intent.EXTRA_SUBJECT, "优美时光");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> matches = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        ResolveInfo info = null;
        for (ResolveInfo each : matches) {
            String pkgName = each.activityInfo.applicationInfo.packageName;
            if (packageName.equals(pkgName)) {
                info = each;
                break;
            }
        }
        if (info == null) {
            ToastUtil.showToast(context, "尚未安装该应用");
            return;
        } else {
            intent.setClassName(packageName, info.activityInfo.name);
        }
        context.startActivity(intent);
    }
    public static void sharePicAll(Context context, ComponentName comp, String shareContent, List<File> images) {
        ArrayList<Uri> picUriList = new ArrayList<>();
        for (File file : images) {
            if (file.exists()) {
                Uri picUri = Uri.fromFile(file);
                picUriList.add(picUri);
            }
        }
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, picUriList);
        intent.putExtra("Kdescription", shareContent);
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        intent.setComponent(comp);
        context.startActivity(intent);
    }
}
