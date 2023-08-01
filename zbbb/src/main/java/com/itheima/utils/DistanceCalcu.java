package com.itheima.utils;

public class DistanceCalcu {
    public static void main(String[] args) {
        double latitude1 = 43.833104;
        double longitude1 = 125.390095;
        double latitude2 = 43.860357;
        double longitude2 = 125.378100;

        double distance = haversineDistance(latitude1, longitude1, latitude2, longitude2);
        System.out.printf("直线距离：%.2f 千米\n", distance);
    }

    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        // 将经纬度转换为弧度
        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);

        // Haversine公式计算距离
        double dLat = radLat2 - radLat1;
        double dLon = radLon2 - radLon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double radiusOfEarth = 6371; // 地球平均半径（单位：千米）
        double distance = radiusOfEarth * c;

        return distance;
    }
}
