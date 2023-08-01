package com.itheima.utils;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

public class GoogleWalkDistanceCalculator {
    public static void main(String[] args) {
        String apiKey = "AIzaSyBPCJHVNE2hcQjfVecxficsBYTW-IZLv9s"; // 替换为您自己的API密钥
        double originLatitude = 43.833104;
        double originLongitude = 125.390095;
        double destinationLatitude = 43.860357;
        double destinationLongitude = 125.378100;

        double distance = calculateWalkDistance(apiKey, originLatitude, originLongitude, destinationLatitude, destinationLongitude);
        System.out.printf("实际步行距离：%.2f 千米\n", distance);
    }

    public static double calculateWalkDistance(String apiKey, double originLat, double originLng, double destLat, double destLng) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();

        try {
            DirectionsApiRequest request = DirectionsApi.newRequest(context)
                    .origin(new com.google.maps.model.LatLng(originLat, originLng))
                    .destination(new com.google.maps.model.LatLng(destLat, destLng))
                    .mode(TravelMode.WALKING);

            DirectionsResult result = request.await();

            if (result.routes.length > 0 && result.routes[0].legs.length > 0) {
                return result.routes[0].legs[0].distance.inMeters / 1000.0; // 转换为千米
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // 计算失败
    }
}
