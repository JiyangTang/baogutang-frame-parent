package com.baogutang.frame.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GeoLbsUtil {

    /**
     * 平均半径,单位：m；不是赤道半径。赤道为6378左右
     */
    private static final double EARTH_RADIUS = 6371000;

    public static void main(String[] args) {
        System.out.println(getDistance("31.163381", "121.397918", "31.163914", "121.398431"));
    }

    /**
     * 获取两坐标之间距离，默认返回米
     *
     * @param lat1 源-纬度
     * @param lng1 源—经度
     * @param lat2 目标-纬度
     * @param lng2 目标-经度
     * @return double
     */
    public static double getDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
        double radiansAX = Math.toRadians(lng1); // A经弧度
        double radiansAY = Math.toRadians(lat1); // A纬弧度
        double radiansBX = Math.toRadians(lng2); // B经弧度
        double radiansBY = Math.toRadians(lat2); // B纬弧度

        // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
                + Math.sin(radiansAY) * Math.sin(radiansBY);
        //        System.out.println("cos = " + cos); // 值域[-1,1]
        double acos = Math.acos(cos); // 反余弦值
        //        System.out.println("acos = " + acos); // 值域[0,π]
        //        System.out.println("∠AOB = " + Math.toDegrees(acos)); // 球心角 值域[0,180]
        // 最终结果
        double distance = EARTH_RADIUS * acos;
        BigDecimal bg = new BigDecimal(distance);
        return bg.setScale(0, RoundingMode.HALF_UP).doubleValue();
    }


    public static BigDecimal getDistance(String lat1, String lng1, String lat2, String lng2) {
        if (StringUtils.isAnyEmpty(lat1, lat2, lng1, lng2)) {
            //任意一个为空 返回null
            return null;
        }
        double distance = getDistance(Double.valueOf(lat1), Double.valueOf(lng1), Double.valueOf(lat2), Double.valueOf(lng2));
        return BigDecimal.valueOf(distance).setScale(0, RoundingMode.DOWN);
    }


}