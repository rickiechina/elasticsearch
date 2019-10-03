package com.rickie.elasticsearch.transportclientdemo.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class TransportClientConfig {
    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.cluster-name}")
    private String cluster;

    @Bean(value = "transportClient", destroyMethod = "close")
    public TransportClient client() {
        try{
            // 设置ES实例的名称
            Settings settings = Settings.builder().put("cluster.name", cluster).build();

            // 创建客户端，如果使用默认配置，传参为 Settings.Empty
            TransportClient client = new PreBuiltTransportClient(settings);

            // 添加节点
            String[] hosts = host.split(";");
            for(String host : hosts) {
                String ip = host.split(":")[0];
                int port = Integer.valueOf(host.split(":")[1]);

                // 创建节点
                TransportAddress node = new TransportAddress(InetAddress.getByName(ip), port);
                client.addTransportAddress(node);
            }

            System.out.println("Elasticsearch Client 连接成功");
            return client;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }
}
