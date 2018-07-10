package com.umeitime.common.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

import com.google.gson.Gson;
import com.umeitime.common.AppContext;
import com.umeitime.common.model.UserInfo;
import com.umeitime.common.tools.SPUtil;
import com.umeitime.common.tools.StringUtils;

/**
 * Created by hujunwei on 2018/4/22.
 */

public class UserInfoDataManager {
    public static String USERINFO = "USERINFO";

    public static UserInfo getUserInfo(Context mContext) {
        UserInfo userInfo = new UserInfo();
        SharedPreferences preferences = mContext.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
        String jsonData = preferences.getString(USERINFO, "");
        if (StringUtils.isNotBlank(jsonData)) {
            userInfo = new Gson().fromJson(jsonData, UserInfo.class);
        }
        return userInfo;
    }
    public static void clearUserInfo(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
    public static void saveUserInfo(UserInfo userInfo) {
        if (userInfo.uid == 0) {
            SPUtil.put(AppContext.getInstance().getApplicationContext(), "UserInfo", "");
        } else {
            SPUtil.put(AppContext.getInstance().getApplicationContext(), "UserInfo", new Gson().toJson(userInfo));
        }
    }

    public static UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        String jsonData = (String) SPUtil.get(AppContext.getInstance().getApplicationContext(), "UserInfo", "");
        if (StringUtils.isNotBlank(jsonData)) {
            userInfo = new Gson().fromJson(jsonData, UserInfo.class);
        }
        return userInfo;
    }
    public static void saveUserPageInfo(UserInfo userInfo) {
        SPUtil.put(AppContext.getInstance().getApplicationContext(), "UserPageInfo_" + userInfo.uid, new Gson().toJson(userInfo));
    }

    public static UserInfo getUserPageInfo(int uid) {
        String jsonData = (String) SPUtil.get(AppContext.getInstance().getApplicationContext(),"UserPageInfo_" + uid, "");
        if (StringUtils.isNotBlank(jsonData)) {
            try {
                return new Gson().fromJson(jsonData, UserInfo.class);
            } catch (Exception e) {
            }
        }
        return null;
    }
}
