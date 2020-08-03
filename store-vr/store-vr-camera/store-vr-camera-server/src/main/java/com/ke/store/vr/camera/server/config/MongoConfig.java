package com.ke.store.vr.camera.server.config;

import com.ke.store.vr.common.config.mongo.BigDecimalToDecimal128Converter;
import com.ke.store.vr.common.config.mongo.Decimal128ToBigDecimalConverter;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author axin
 * @since 2020-06-30
 * @summary mongo 配置类
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MongoConfig.MongoClientOptionProperties.class)
public class MongoConfig {

    /**
     * monogo 转换器
     * @return
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory,
                                                       MongoMappingContext context, BeanFactory beanFactory, MongoCustomConversions conversions) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        // remove _class field
//    mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mappingConverter.setCustomConversions(conversions);
        return mappingConverter;
    }

    /**
     * mongo 金额 转换
     * @return
     */
    @Bean
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new BigDecimalToDecimal128Converter());
        converters.add(new Decimal128ToBigDecimalConverter());
        return new CustomConversions(converters);
    }

    /**
     * 自定义mongo连接池
     * @param properties
     * @return
     */
    @Bean
    public MongoDbFactory mongoDbFactory(MongoClientOptionProperties properties) {
        //创建客户端参数
        MongoClientOptions options = mongoClientOptions(properties);

        //创建客户端和Factory
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for (String address : properties.getAddress()) {
            String[] hostAndPort = address.split(":");
            String host = hostAndPort[0];
            Integer port = Integer.parseInt(hostAndPort[1]);
            ServerAddress serverAddress = new ServerAddress(host, port);
            serverAddresses.add(serverAddress);
        }

        //创建认证客户端
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(properties.getUsername(),
                properties.getAuthenticationDatabase() != null ? properties.getAuthenticationDatabase() : properties.getDatabase(),
                properties.getPassword().toCharArray());

        MongoClient mongoClient = new MongoClient(serverAddresses.get(0), mongoCredential, options);
        //集群模式
        if (serverAddresses.size() > 1) {
            mongoClient = new MongoClient(serverAddresses, new ArrayList<>(Arrays.asList(mongoCredential)));
        }
        /** ps: 创建非认证客户端*/
        //MongoClient mongoClient = new MongoClient(serverAddresses, mongoClientOptions);
        return new SimpleMongoDbFactory(mongoClient, properties.getDatabase());
    }

    /**
     * mongo客户端参数配置
     * @return
     */
    public MongoClientOptions mongoClientOptions(MongoClientOptionProperties properties) {
        return MongoClientOptions.builder()
                .connectTimeout(properties.getConnectionTimeoutMs())
                .socketTimeout(properties.getReadTimeoutMs()).applicationName(properties.getClientName())
                .heartbeatConnectTimeout(properties.getHeartbeatConnectionTimeoutMs())
                .heartbeatSocketTimeout(properties.getHeartbeatReadTimeoutMs())
                .heartbeatFrequency(properties.getHeartbeatFrequencyMs())
                .minHeartbeatFrequency(properties.getMinHeartbeatFrequencyMs())
                .maxConnectionIdleTime(properties.getConnectionMaxIdleTimeMs())
                .maxConnectionLifeTime(properties.getConnectionMaxLifeTimeMs())
                .maxWaitTime(properties.getPoolMaxWaitTimeMs())
                .connectionsPerHost(properties.getConnectionsPerHost())
                .threadsAllowedToBlockForConnectionMultiplier(
                        properties.getThreadsAllowedToBlockForConnectionMultiplier())
                .minConnectionsPerHost(properties.getMinConnectionsPerHost()).build();
    }

    @Getter
    @Setter
    @Validated
    @ConfigurationProperties(prefix = "mongodb")
    public static class MongoClientOptionProperties {

        /** 基础连接参数 */
        private String database;
        private String username;
        private String password;
        @NotNull
        private List<String> address;
        private String authenticationDatabase;

        /** 客户端连接池参数 */
        @NotNull
        @Size(min = 1)
        private String clientName;
        /** socket连接超时时间 */
        @Min(value = 1)
        private int connectionTimeoutMs;
        /** socket读取超时时间 */
        @Min(value = 1)
        private int readTimeoutMs;
        /** 连接池获取链接等待时间 */
        @Min(value = 1)
        private int poolMaxWaitTimeMs;
        /** 连接闲置时间 */
        @Min(value = 1)
        private int connectionMaxIdleTimeMs;
        /** 连接最多可以使用多久 */
        @Min(value = 1)
        private int connectionMaxLifeTimeMs;
        /** 心跳检测发送频率 */
        @Min(value = 2000)
        private int heartbeatFrequencyMs;

        /** 最小的心跳检测发送频率 */
        @Min(value = 300)
        private int minHeartbeatFrequencyMs;
        /** 计算允许多少个线程阻塞等待时的乘数，算法：threadsAllowedToBlockForConnectionMultiplier*connectionsPerHost */
        @Min(value = 1)
        private int threadsAllowedToBlockForConnectionMultiplier;
        /** 心跳检测连接超时时间 */
        @Min(value = 200)
        private int heartbeatConnectionTimeoutMs;
        /** 心跳检测读取超时时间 */
        @Min(value = 200)
        private int heartbeatReadTimeoutMs;

        /** 每个host最大连接数 */
        @Min(value = 1)
        private int connectionsPerHost;
        /** 每个host的最小连接数 */
        @Min(value = 1)
        private int minConnectionsPerHost;
    }
}
