package cn.crbz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 消费者
 *
 * @author Chopper
 * @since 2020/11/16 10:03 下午
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConsumerApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        System.setProperty("rocketmq.client.logUseSlf4j","true");
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
