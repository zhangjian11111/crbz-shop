package com.itheima.utils;

import java.lang.Math;

public class InKilometer {
    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        // 将经纬度转换为弧度
        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);

        // Haversine公式计算
        double dlat = radLat2 - radLat1;
        double dlon = radLon2 - radLon1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 地球半径（单位：公里）
        double radius = 6371.0;

        // 计算距离并返回结果
        double distance = radius * c;
        return distance;
    }

    public static void main(String[] args) {
        // 中心经纬度125.390095,43.833104
        double centerLat = 125.390095;
        double centerLon = 43.833104;

        // 要判断的经纬度
        double testLat = 125.319895;
        double testLon = 43.900013;

        // 计算距离
        double distanceKm = haversineDistance(centerLat, centerLon, testLat, testLon);

        // 判断是否在一公里范围内
        if (distanceKm <= 3.0) {
            System.out.println("在中心三公里范围内。");
        } else {
            System.out.println("不在中心三公里范围内。");
        }
    }
}
