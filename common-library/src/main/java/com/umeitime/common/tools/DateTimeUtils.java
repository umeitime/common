package com.umeitime.common.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author June
 * @date 2017/5/31
 * @email jayfans@qq.com
 * @packagename wanglijun.vip.androidutils
 * @description 时间转换工具类
 */
public class DateTimeUtils {
    public static String getDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }
    public static String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());
    }
    /**
     * 字符串转换到时间格式
     *
     * @param dateStr   需要转换的字符串
     * @param formatStr 需要格式的目标字符串  举例 yyyyMMdd
     * @return String 返回转换后的时间字符串
     * @throws ParseException 转换异常
     */
    public static String StringToDate(String dateStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat(formatStr);

        return s.format(date);
    }

    public static String StringToDate2(long dateStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(String.valueOf(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat(formatStr);

        return s.format(date);
    }

    //Datetime转化为时间戳
    public static long dateTime2Timestamp(String datetime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(datetime);
            return date.getTime() / 1000;
        } catch (Exception e) {
            return new Date().getTime() / 1000;
        }
    }

    public static Date str2Date(String sdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(sdate.replace(".0", ""));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     */
    public static String getStandardDate(String d) {
        try {
            String str = "";
            Calendar before = Calendar.getInstance();
            Calendar current = Calendar.getInstance();
            before.setTime(str2Date(d));
            current.setTime(new Date());
            int minutes = current.get(Calendar.MINUTE) - before.get(Calendar.MINUTE);
            int hours = current.get(Calendar.HOUR_OF_DAY) - before.get(Calendar.HOUR_OF_DAY);
            int days = current.get(Calendar.DATE) - before.get(Calendar.DATE);
            int year = current.get(Calendar.YEAR) - before.get(Calendar.YEAR);
            int month = current.get(Calendar.MONTH) - before.get(Calendar.MONTH);
            String MD = StringToDate(d, "MM-dd");
            String HM = StringToDate(d, "HH:mm");
            if (year >= 1) {
                str = StringToDate(d, "yyyy-MM-dd");
            } else if (year == 0) {
                if (month > 0||days > 1) {
                    str = MD + " " + HM;
                } else if (days == 1) {
                    str = "昨天" + HM;
                } else if (days == 0) {
                    if (hours > 0) {
                        str = HM;
                    } else {
                        if (minutes > 0) {
                            str = minutes + "分钟前";
                        } else {
                            str = "刚刚";
                        }
                    }
                }
            }
            return str;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public static String getStandardDate2(String d) {
        try {
            String str = "";
            Calendar before = Calendar.getInstance();
            Calendar current = Calendar.getInstance();
            before.setTime(str2Date(d));
            current.setTime(new Date());
            int minutes = current.get(Calendar.MINUTE) - before.get(Calendar.MINUTE);
            int hours = current.get(Calendar.HOUR_OF_DAY) - before.get(Calendar.HOUR_OF_DAY);
            int days = current.get(Calendar.DATE) - before.get(Calendar.DATE);
            int year = current.get(Calendar.YEAR) - before.get(Calendar.YEAR);
            int month = current.get(Calendar.MONTH) - before.get(Calendar.MONTH);
            String YMD = StringToDate(d, "yyyy-MM-dd");
            String MD = StringToDate(d, "MM-dd");
            if (year >0) {
                str = YMD;
            } else if (year == 0) {
               if (days >0 ) {
                    str = MD;
                } else if (days == 0) {
                    if (hours > 0) {
                        str = hours + "小时前";
                    } else {
                        if (minutes > 0) {
                            str = minutes + "分钟前";
                        } else {
                            str = "刚刚";
                        }
                    }
                }
            }
            return str;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public static String getStandardDate3(String d) {
        try {
            String str = "";
            Calendar before = Calendar.getInstance();
            Calendar current = Calendar.getInstance();
            before.setTime(str2Date(d));
            current.setTime(new Date());
            int minutes = current.get(Calendar.MINUTE) - before.get(Calendar.MINUTE);
            int hours = current.get(Calendar.HOUR_OF_DAY) - before.get(Calendar.HOUR_OF_DAY);
            int days = current.get(Calendar.DATE) - before.get(Calendar.DATE);
            int year = current.get(Calendar.YEAR) - before.get(Calendar.YEAR);
            int month = current.get(Calendar.MONTH) - before.get(Calendar.MONTH);
            if (year >0) {
                str = year+"年前";
            } else if (year == 0) {
                if (month > 0) {
                    str = month+"月前";
                } else if (days >1 ) {
                    str = days+"天前";
                } else if (days ==1 ) {
                    str = "昨天";
                } else if (days ==2 ) {
                    str = "前天";
                } else if (days == 0) {
                    if (hours > 0) {
                        str = hours + "小时前";
                    } else {
                        if (minutes > 0) {
                            str = minutes + "分钟前";
                        } else {
                            str = "刚刚";
                        }
                    }
                }
            }
            return str;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
}
