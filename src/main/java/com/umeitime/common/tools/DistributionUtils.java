package com.umeitime.common.tools;

import com.umeitime.common.model.LocationBean;

import java.math.BigDecimal;
/**
 * Created by hujunwei on 16/10/5.
 */

public class DistributionUtils {
    /*
     * 计算两点之间距离
     *
     * @param start
     *
     * @param end
     *
     * @return 米
     */
    public static String getDistance(LocationBean start, LocationBean end) {
        if(start.latitude==end.latitude&&start.longitude==end.longitude)return "0.0km";
        double lon1 = (Math.PI / 180) * start.longitude;
        double lon2 = (Math.PI / 180) * end.longitude;
        double lat1 = (Math.PI / 180) * start.latitude;
        double lat2 = (Math.PI / 180) * end.latitude;

        // double Lat1r = (Math.PI/180)*(gp1.getLatitudeE6()/1E6);
        // double Lat2r = (Math.PI/180)*(gp2.getLatitudeE6()/1E6);
        // double Lon1r = (Math.PI/180)*(gp1.getLongitudeE6()/1E6);
        // double Lon2r = (Math.PI/180)*(gp2.getLongitudeE6()/1E6);
        // 地球半径
        double R = 6371;

        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        BigDecimal bg = new BigDecimal(d);
        if(d<100){
            double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return  f1 + "km";
        }else{
            return (int)d + "km";
        }

    }
}
