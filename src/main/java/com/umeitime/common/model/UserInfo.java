package com.umeitime.common.model;

/**
 * Created by hujunwei on 17/7/10.
 */

public class UserInfo extends UserBean {
    public String userId="";//手机号
    public String qqId="";//
    public String weiboId="";
    public String flymeId="";
    public String weibo = "";
    public String regTime = "";
    public int follow;
    public String birthday="";
    public String qq="";
    public int wyCount;
    public int regType;//0手机号 1qq 2微博 3flyme 4微信
    public int fansCount;
    public int followCount;
    public int userState=1;
    public int albumnum=0;
    public String imei="";
    public String phoneModel="";
    public long saveTime;
}
