package com.bank.produces.automationproducer.config;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {
    public static String KAFKAHOST = "192.168.1.52:9092";
    public static String SERVICESDB = "192.168.1.52:9002";
    public static String REDIS = "automation-redis";
    public static int REDISPORT = 6379;
    public static String REDISTOPIC = "";

    public static List<String> TOPICDEVICES = new ArrayList<>();

    public static Properties KafkaConfig(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.KAFKAHOST);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    public static RedisConnection<String, String> REDISCONF(){
        RedisClient redisClient = new RedisClient(RedisURI.create("redis://"+Config.REDIS+":"+Config.REDISPORT));
        RedisConnection<String, String> connectin = redisClient.connect();
        return connectin;
    }
}
