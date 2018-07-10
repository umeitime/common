package com.umeitime.common.model;

import com.umeitime.common.tools.StringUtils;

import java.io.Serializable;

/**
 * Created by hujunwei on 2018/7/4.
 */

public class UserBean implements Serializable{
    public int uid;
    public String unionid="";
    public String openid="";
    public String userName="";
    public String userHead="";
    public String userSign = "";
    public String userGender = "1";
    public String userAddress = "";
    public String userCover = "";
    public String token = "";

    public String getUserSign() {
        if(StringUtils.isBlank(userSign)){
            return "最动听的声音叫时光。";
        }
        return userSign;
    }
}
