package cn.crbz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 基础API
 *
 * @author Chopper
 * @since 2020/11/17 3:38 下午
 */
@EnableCaching
@SpringBootApplication
@EnableDiscoveryClient
public class CommonApiApplication {

    public static void main(String[] args) {

        System.setProperty("nacos.logging.default.config.enabled","false");
        System.setProperty("rocketmq.client.logUseSlf4j","true");
        SpringApplication.run(CommonApiApplication.class, args);
    }

}
