package com.umeitime.common.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;

import java.lang.reflect.Field;

/**
 * @author June
 * @date 2017/5/31
 * @email jayfans@qq.com
 * @packagename com.umeitime.common.tools
 * @desc: 屏幕相关
 */

public class DisplayUtils {
    /**
     * 是否横屏
     *
     * @param context
     * @return
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 是否竖屏
     *
     * @param context
     * @return
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * Get screen width, in pixels
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * Get screen height, in pixels
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * Get screen density, the logical density of the display
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * Get screen density dpi, the screen density expressed as dots-per-inch
     *
     * @param context
     * @return
     */
    public static int getScreenDensityDPI(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    /**
     * Get titlebar height, this method cannot be used in onCreate(),onStart(),onResume(), unless it is called in the
     * post(Runnable).
     *
     * @param activity
     * @return
     */
    public static int getTitleBarHeight(Activity activity) {
        int statusBarHeight = getStatusBarHeight(activity);
        int contentViewTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;
        return titleBarHeight < 0 ? 0 : titleBarHeight;
    }

    // 获取手机状态栏高度
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
    // 获取ActionBar的高度
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))// 如果资源是存在的、有效的
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
    //获取底部 navigation bar 高度
    public static int getNavigationBarHeight(Context mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("dbw", "Navi height:" + height);
        return height;}

    /**
     * Convert dp to px by the density of phone
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dip2px(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return (int) (dipToPx(context, dp) + 0.5f);
    }

    /**
     * Convert dp to px
     *
     * @param context
     * @param dp
     * @return
     */
    private static float dipToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale;
    }

    /**
     * Convert px to dp by the density of phone
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dip(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return (int) (pxToDip(context, px) + 0.5f);
    }

    /**
     * Convert px to dp
     *
     * @param context
     * @param px
     * @return
     */
    private static float pxToDip(Context context, float px) {
        if (context == null) {
            return -1;
        }
        float scale = context.getResources().getDisplayMetrics().density;
        return px / scale;
    }

    /**
     * Convert px to sp
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2sp(Context context, float px) {
        return (int) (pxToSp(context, px) + 0.5f);
    }

    /**
     * Convert px to sp
     *
     * @param context
     * @param px
     * @return
     */
    private static float pxToSp(Context context, float px) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return px / fontScale;
    }

    /**
     * Convert sp to px
     *
     * @param context
     * @param sp
     * @return
     */
    public static int sp2px(Context context, float sp) {
        return (int) (spToPx(context, sp) + 0.5f);
    }

    /**
     * Convert sp to px
     *
     * @param context
     * @param sp
     * @return
     */
    private static float spToPx(Context context, float sp) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * fontScale;
    }
}
