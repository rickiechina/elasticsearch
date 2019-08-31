package com.rickie.elasticsearch.restclientdemo;

import com.rickie.elasticsearch.restclientdemo.config.EsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({EsConfig.class})
public class RestClientDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestClientDemoApplication.class, args);
    }

}
