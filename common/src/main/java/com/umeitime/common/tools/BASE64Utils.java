package com.umeitime.common.tools;

import android.util.Base64;

/**
 * @author June
 * @date 2017/5/31
 * @email jayfans@qq.com
 * @packagename com.umeitime.common.tools
 * @desc: bese64的转换
 */

public class BASE64Utils {
    /**
     * base64编码
     *
     * @param input
     * @return
     */
    public static byte[] encodeBase64(byte[] input) {
        return Base64.encode(input, Base64.DEFAULT);
    }

    /**
     * base64编码
     *
     * @param s
     * @return
     */
    public static String encodeBase64(String s) {
        return Base64.encodeToString(s.getBytes(), Base64.DEFAULT);
    }

    /**
     * base64解码
     *
     * @param input
     * @return
     */
    public static byte[] decodeBase64(byte[] input) {
        return Base64.decode(input, Base64.DEFAULT);
    }

    /**
     * base64解码
     *
     * @param s
     * @return
     */
    public static String decodeBase64(String s) {
        return new String(Base64.decode(s, Base64.DEFAULT));
    }
}
