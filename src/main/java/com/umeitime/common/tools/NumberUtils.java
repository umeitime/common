package com.umeitime.common.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author June
 * @date 2017/5/31
 * @email jayfans@qq.com
 * @packagename com.umeitime.common.tools
 * @desc: 数字格式化操作类
 */

public class NumberUtils {

    /**
     * 保留一位小数
     *
     * @param number
     * @return
     */
    public static String formatOneDecimal(float number) {
        DecimalFormat oneDec = new DecimalFormat("##0.0");
        return oneDec.format(number);
    }

    /**
     * 保留两位小数
     *
     * @param number
     * @return
     */
    public static String formatTwoDecimal(float number) {
        DecimalFormat twoDec = new DecimalFormat("##0.00");
        return twoDec.format(number);
    }

    /**
     * 保留两位小数百分比
     *
     * @param number
     * @return
     */
    public static String formatTwoDecimalPercent(float number) {
        return formatTwoDecimal(number) + "%";
    }

    /**
     * 四舍五入
     *
     * @param number
     * @param scale  scale of the result returned.
     * @return
     */
    public static double roundingNumber(float number, int scale) {
        return roundingNumber(number, scale, RoundingMode.HALF_UP);
    }

    /**
     * 四舍五入
     *
     * @param number
     * @param scale        scale of the result returned.
     * @param roundingMode rounding mode to be used to round the result.
     * @return
     */
    public static double roundingNumber(float number, int scale, RoundingMode roundingMode) {
        BigDecimal b = new BigDecimal(number);
        return b.setScale(scale, roundingMode).doubleValue();
    }
    /**
     * 格式化时间单元(时、分、秒)
     *    小于10的话在十位上补0，如传入2的话返回02，传入12的话返回12
     * @param time
     *                  播放时间
     * @return 格式化后的时间,如(02)
     */
    public static String formatTimeUnit(long time) {
        return time < 10 ? "0" + time :  time+"";
    }
    /**
     * @param format_time
     * @return (时:分:秒)格式的时间格式，如(00:03:00)
     */
    public static String formatTimeString(long format_time) {
        String minutes=formatTimeUnit(format_time / 60); //分
        String seconds=formatTimeUnit(format_time % 60); //秒
        return  minutes+ ":" + seconds;
    }

}
