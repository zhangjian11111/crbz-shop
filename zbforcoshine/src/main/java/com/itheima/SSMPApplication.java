package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SSMPApplication {

    public static void main(String[] args) {
        SpringApplication.run(SSMPApplication.class, args);
    }

}
