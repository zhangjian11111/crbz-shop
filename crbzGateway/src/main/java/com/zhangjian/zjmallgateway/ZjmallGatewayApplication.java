package com.zhangjian.zjmallgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient
public class ZjmallGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZjmallGatewayApplication.class, args);
    }

}
