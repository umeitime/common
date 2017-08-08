package com.umeitime.common.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * @author June
 * @date 2017/3/28
 * @email jayfans@qq.com
 * @packagename wanglijun.vip.androidutils
 * @description Toast封装类
 */
public class ToastUtil {
    private static Toast toast;

    /**
     * 强大的吐司，能够连续弹的吐司
     *
     * @param text
     */
    public static void showToast(Context context, String text) {
        if(StringUtils.isBlank(text))return;
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);//如果不为空，则直接改变当前toast的文本
        }
        toast.show();
    }
    public static void showToast(Context context, int res) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getString(res), Toast.LENGTH_SHORT);
        } else {
            toast.setText(context.getString(res));//如果不为空，则直接改变当前toast的文本
        }
        toast.show();
    }
}
