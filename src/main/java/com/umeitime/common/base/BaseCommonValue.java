package com.umeitime.common.base;
import com.umeitime.common.tools.StringUtils;
import java.io.File;
/**
 * Created by hujunwei on 17/7/20.
 */
public class BaseCommonValue {
    public static final int QINIU_EXPIRE_TIME = 3600;//单位秒
    public static final int TOKEN_EXPRIED = -101;
    public static final int GSON_FORMAT_ERROR = -102;
    public static final int WEB_RESP_CODE_SUCCESS = 200;
    public static final String UMEI = " (分享自@优美时光)";
    public static final String Bucket = "umeitime";
    public static String USER_TOKEN = "";
    public static String QINIU_URL = "http://img.umeitime.com";
    public static String ROOT_URL = "http://172.16.37.33";
//    public static String ROOT_URL = "http://www.umeitime.com";
    public static String CHAT_SERVER_URL = ROOT_URL+":9093/";
    public static String API_SERVER_URL = ROOT_URL+":8083/server/";
    public static String APP_URL = "http://www.umeitime.com/weiyu/#/word?id=";
    public static String[] words = new String[]{
            "代理",
            "想做兼职",
            "加盟",
            "进化不完全的生命体",
            "震死他们",
            "淘宝店铺",
            "死全家",
            "在家挣钱",
            "taobao.com",
            "贱人",
            "月入",
            "装b",
            "大sb",
            "傻逼",
            "傻b",
            "煞逼",
            "煞笔",
            "刹笔",
            "傻比",
            "沙比",
            "欠干",
            "婊子养的",
            "我日你",
            "我操",
            "我草",
            "卧艹",
            "发春",
            "发情",
            "卧槽",
            "爆你菊",
            "艹你",
            "cao你",
            "你他妈",
            "真他妈",
            "别他吗",
            "草你吗",
            "草你丫",
            "操你妈",
            "擦你妈",
            "操你娘",
            "操他妈",
            "日你妈",
            "干你妈",
            "干你娘",
            "娘西皮",
            "狗操",
            "狗草",
            "狗杂种",
            "狗日的",
            "操你祖宗",
            "操你全家",
            "操你大爷",
            "妈逼",
            "你麻痹",
            "麻痹的",
            "妈了个逼",
            "马勒",
            "狗娘养",
            "贱比",
            "贱b",
            "下贱",
            "死全家",
            "全家死光",
            "全家不得好死",
            "全家死绝",
            "白痴",
            "无耻",
            "sb",
            "杀b",
            "你吗b",
            "你妈的",
            "婊子",
            "贱货",
            "人渣",
            "混蛋",
            "媚外",
            "和弦",
            "兼职",
            "性伴侣",
            "男公关",
            "火辣",
            "精子",
            "射精",
            "诱奸",
            "强奸",
            "做爱",
            "性爱",
            "发生关系",
            "按摩",
            "快感",
            "处男",
            "猛男",
            "少妇",
            "屁股",
            "下体",
            "a片",
            "内裤",
            "浑圆",
            "咪咪",
            "发情",
            "刺激",
            "白嫩",
            "粉嫩",
            "兽性",
            "风骚",
            "呻吟",
            "sm",
            "阉割",
            "高潮",
            "裸露",
            "不穿",
            "一丝不挂",
            "脱光",
            "干你",
            "干死",
            "我干",
            "专业代理",
            "帮忙点一下",
            "帮忙点下",
            "请点击进入",
            "详情请进入",
            "私人侦探",
            "私家侦探",
            "针孔摄象",
            "调查婚外情",
            "信用卡提现",
            "无抵押贷款",
            "广告代理",
            "原音铃声",
            "借腹生子",
            "找个妈妈",
            "找个爸爸",
            "代孕妈妈",
            "代生孩子",
            "代开发票",
            "腾讯客服电话",
            "销售热线",
            "免费订购热线",
            "低价出售",
            "款到发货",
            "回复可见",
            "连锁加盟",
            "加盟连锁",
            "免费二级域名",
            "蚁力神",
            "婴儿汤",
            "售肾",
            "刻章办",
            "买小车",
            "套牌车",
            "txt下载",
            "六位qq",
            "6位qq",
            "位的qq",
            "个qb",
            "送qb",
            "用刀横向切腹",
            "完全自杀手册",
            "四海帮",
            "足球投注",
            "地下钱庄",
            "中国复兴党",
            "阿波罗网",
            "曾道人",
            "六合彩",
            "改卷内幕",
            "替考试",
            "隐形耳机",
            "出售答案",
            "资金短缺",
            "质押贷款",
            "小额贷款",
            "网络兼职",
            "招聘",
            "日赚",
    };

    //public static String API_SERVER_URL = "http://192.168.0.102:8081/server/";
    public static String getImageUrl(String url, int width, int height) {
        if(StringUtils.isBlank(url))return "";
        if (!new File(url).exists() && !url.startsWith("http")) {
            return QINIU_URL + url + "?imageView2/1/w/" + width + "/h/" + height + "/format/webp|imageslim";
        } else if (url.startsWith(QINIU_URL)) {
            return url + "?imageView2/1/w/" + width + "/h/" + height + "/format/webp|imageslim";
        }
        return url;
    }

    public static String getPicUrl(String url) {
        if (StringUtils.isNotBlank(url)) {
            if (new File(url).exists() || url.startsWith("http")) {
                return url;
            } else {
                return QINIU_URL + url;
            }
        }
        return "";
    }
}
