package cn.crbz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


/**
 * @author liushuai
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ImApiApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        System.setProperty("rocketmq.client.logUseSlf4j","true");
        SpringApplication.run(ImApiApplication.class, args);
    }

    /**
     * 如果使用独立的servlet容器，
     * 而不是直接使用springboot的内置容器，
     * 就不要注入ServerEndpointExporter，
     * 因为它将由容器自己提供和管理
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
