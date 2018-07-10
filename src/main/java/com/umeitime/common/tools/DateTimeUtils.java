package com.umeitime.common.tools;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author June
 * @date 2017/5/31
 * @email jayfans@qq.com
 * @packagename wanglijun.vip.androidutils
 * @description 时间转换工具类
 */
public class DateTimeUtils {
    public static final String[] zodiacArr = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };

    public static final String[] constellationArr = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座" };

    public static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
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
    public static String strToDate2(String strDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA).parse(strDate);
            String dateTime = new SimpleDateFormat("MM-dd HH:mm",Locale.CHINA).format(date);
            return dateTime;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return getCurrentDate();
        }
    }
    //获取当前日期
    public static String getCurrentDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);//可以方便地修改日期格式
        return dateFormat.format(now);
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
            Calendar before = Calendar.getInstance();
            Calendar current = Calendar.getInstance();
            before.setTime(str2Date(d));
            current.setTime(new Date());
            int days = current.get(Calendar.DATE) - before.get(Calendar.DATE);
            String HM = StringToDate(d, "HH:mm");
            if (days == 0) {
                return "今天 "+HM;
            }else if(days==1) {
                return "昨天 "+HM;
            }else{
                int year = current.get(Calendar.YEAR) - before.get(Calendar.YEAR);
                return StringToDate(d, year==0?"MM-dd HH:mm":"yyyy-MM-dd HH:mm");
            }
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
    /**
     * 根据日期获取星座
     * @return
     */
    public static String getConstellation(String datetime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            Date date = dateFormat.parse(datetime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            if (day < constellationEdgeDay[month]) {
                month = month - 1;
            }
            if (month >= 0) {
                return constellationArr[month];
            }
            // default to return 魔羯
            return constellationArr[11];
        }catch (Exception e){

        }
        return "未知";
    }
    public static int getAge(String date) {
        Date dateOfBirth = null;
        int age = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            dateOfBirth = dateFormat.parse(date);
            Calendar born = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            if (dateOfBirth != null) {
                now.setTime(new Date());
                born.setTime(dateOfBirth);
                if (born.after(now)) {
                    throw new IllegalArgumentException(
                            "Can't be born in the future");
                }
                age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
                    age -= 1;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return age;
    }
    public static String strToDate(String strDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA).parse(strDate);
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA).format(date);
            return dateTime;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return getCurrentDate();
        }
    }
}
