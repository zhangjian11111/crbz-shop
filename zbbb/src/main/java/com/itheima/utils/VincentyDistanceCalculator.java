package com.itheima.utils;

import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;

public class VincentyDistanceCalculator {
    public static void main(String[] args) {
        double latitude1 = 43.833104;
        double longitude1 = 125.390095;
        //中日联谊医院
//        double latitude2 = 43.860357;
//        double longitude2 = 125.378100;
        //黎川县第一中学
//        double latitude2 = 27.279126;
//        double longitude2 = 116.916038;

        //胜利公园
//        double latitude2 = 43.900013;
//        double longitude2 = 125.319895;
        //净月南环小学/
        double latitude2 = 43.817564;
        double longitude2 = 125.372297;
        double distance = vincentyDistance(latitude1, longitude1, latitude2, longitude2);
        System.out.printf("曲线距离：%.3f 千米\n", distance);
    }

    public static double vincentyDistance(double lat1, double lon1, double lat2, double lon2) {
        Geodesic geodesic = Geodesic.WGS84;
        GeodesicData geodesicData = geodesic.Inverse(lat1, lon1, lat2, lon2);
        double distance = geodesicData.s12 / 1000.0; // 将结果转换为千米
        return distance;
    }
}
