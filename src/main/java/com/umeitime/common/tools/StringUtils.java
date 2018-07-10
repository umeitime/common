package com.umeitime.common.tools;

import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeitime.common.base.BaseCommonValue;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.util.Patterns.GOOD_IRI_CHAR;
import static android.util.Patterns.TOP_LEVEL_DOMAIN_STR_FOR_WEB_URL;

/**
 * @author June
 * @date 2017/5/31
 * @email jayfans@qq.com
 * @packagename com.umeitime.common.tools
 * @desc: 字符串操作
 */

public class StringUtils {
    public static final Pattern WEB_URL = Pattern
            .compile("((?:(http|https|Http|Https|rtsp|Rtsp|ftp|Ftp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)"
                    + "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_"
                    + "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?"
                    + "((?:(?:["
                    + GOOD_IRI_CHAR
                    + "]["
                    + GOOD_IRI_CHAR
                    + "\\-]{0,64}\\.)+" // named host
                    + TOP_LEVEL_DOMAIN_STR_FOR_WEB_URL
                    + "|(?:(?:25[0-5]|2[0-4]" // or ip address
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]"
                    + "|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}" + "|[1-9][0-9]|[0-9])))"
                    + "(?:\\:\\d{1,5})?)" // plus option port number
                    + "(\\/(?:(?:[" + GOOD_IRI_CHAR + "\\;\\/\\?\\:\\@\\&\\=\\#\\~" // plus
                    + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])*[-A-Z0-9+&@#/%=~_|]|(?:\\%[a-fA-F0-9]{2}))*)?", Pattern.CASE_INSENSITIVE);

    /**
     * 字符串是否为空格串
     *
     * @return
     * @RequestParam str
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 字符串是否非空格串
     *
     * @return
     * @RequestParam str
     */
    public static boolean isNotBlank(String str) {
        return (str != null && str.trim().length() != 0);
    }

    /**
     * Judge whether a string is whitespace, empty ("") or null.
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0 || str.equalsIgnoreCase("null") || str.equals("")) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if a and b are equal, including if they are both null.
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        return TextUtils.equals(a, b);
    }

    /**
     * Judge whether a string is number.
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Encode a string
     *
     * @param str
     * @return
     */
    public static String encodeString(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * Decode a string
     *
     * @param str
     * @return
     */
    public static String decodeString(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * Converts this string to lower case, using the rules of {@code locale}.
     *
     * @param s
     * @return
     */
    public static String toLowerCase(String s) {
        return s.toLowerCase(Locale.getDefault());
    }

    /**
     * Converts this this string to upper case, using the rules of {@code locale}.
     *
     * @param s
     * @return
     */
    public static String toUpperCase(String s) {
        return s.toUpperCase(Locale.getDefault());
    }

    public static String getLink(String html) {
        Matcher matcher = WEB_URL.matcher(html);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }


    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }
    public static String toTag(String name){
        return "＃" + name + "＃";
    }
    public static String getCount(int number){
        if(number==0){
            return "";
        }else if(number<10000){
            return number+"";
        }else{
            return  String.format("%.1f%s",1.0*number/10000,"万");
        }
    }

    /**
     * 打开地图App 有经纬度
     * @param lat
     * @param lng
     * @param addr
     * @return
     */
    public static String getAddressDetailUrl(String lat, String lng, String addr) {

        String strUrl = "geo:%1$s,%2$s?q=%3$s";

        return String.format(strUrl, lat, lng ,addr);

    }

    /**
     * 打开地图App 只有地址信息
     * @param addr
     * @return
     */
    public static String getAddressUrl(String addr) {
        String strUrl = "geo:q=%1$s";
        return String.format(strUrl,addr);
    }

    /**
     * 网页版地图 有经纬度
     * @param lat
     * @param lng
     * @param addr
     * @return
     */
    public static String getHtmlAddressDetailUrl(String lat, String lng, String addr) {
        String strUrl = "http://api.map.baidu.com/marker?location=%1$s,%2$s&title=%3$s&content=%4$s&output=html";
        return String.format(strUrl, lat, lng, addr ,addr);
    }

    /**
     * 网页版地图  只有地址
     * @param addr
     * @return
     */
    public static String getHtmlAddressUrl(String addr) {
        String strUrl = "http://api.map.baidu.com/geocoder?address=%1$s&output=html";
        return String.format(strUrl,addr);
    }

    public static void map(Context context){
        String mAddress = "鼓楼大街"  ;
        String mLatitude = "";
        String mLongitude = "";
        Uri mapUri = null;
        if (!isEmpty(mAddress)){
            try {
                /**
                 * 有地图软件
                 */
                if (isEmpty(mLatitude) || isEmpty(mLongitude)){
                    mapUri = Uri.parse(getAddressUrl(mAddress));
                }else{
                    mapUri = Uri.parse(getAddressDetailUrl(mLatitude + "",mLongitude+ "",mAddress));
                }
                Intent loction = new Intent(Intent.ACTION_VIEW, mapUri);
                context.startActivity(loction);
            } catch (ActivityNotFoundException e){
                /**
                 *  没有安装地图软件  会报错  此时打开网页版地图
                 */
                if (isEmpty(mLatitude) || isEmpty(mLongitude)){
                    mapUri = Uri.parse(getHtmlAddressUrl(mAddress));
                }else{
                    mapUri = Uri.parse(getHtmlAddressDetailUrl(mLatitude+ "",mLongitude+ "",mAddress));
                }
                Intent loction = new Intent(Intent.ACTION_VIEW, mapUri);
                context.startActivity(loction);
            } catch (Exception e){
                Toast.makeText(context, "异常", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "地址为空", Toast.LENGTH_SHORT).show();
        }
    }
    //校验URL
    public static boolean isUrl(String url) {
        return url.startsWith("http")||url.startsWith("https");
    }

    public static void clearClipboard(Context context) {
        // 得到剪贴板管理器
        android.text.ClipboardManager cmb = (android.text.ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText("");
    }

    public static boolean isAd(String content) {
        for(String s: BaseCommonValue.words){
            if(content.contains(s)){
                return true;
            }
        }
        return false;
    }
}
