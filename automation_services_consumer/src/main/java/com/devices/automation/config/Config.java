package com.devices.automation.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class Config {
//    public static String KAFKAHOST = "automation_tools_kafka_1:9092";
//    public static String APPIUMSERVER = "172.18.0.1:4723";
//    public static String SERVICESDB = "automation-api:9002";
//    public static String KAFKATOPIC = "9b70ca9f0401";

    public static String KAFKAHOST = "192.168.1.52:9092";
    public static String APPIUMSERVER = "localhost:4723";
    public static String SERVICESDB = "192.168.1.52:9002";
    public static String KAFKATOPIC = "9b94ca1b0401";

    public static Properties KAFKACONFIG(String group){
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKAHOST);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, group);
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }

    public static Properties KAFKAADMIN(){
        Properties properties = new Properties();
        properties.put("bootstrap.servers", KAFKAHOST);
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);
        return properties;
    }
}
