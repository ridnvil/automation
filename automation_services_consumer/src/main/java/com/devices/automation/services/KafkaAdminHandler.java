package com.devices.automation.services;

import com.devices.automation.config.Config;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteRecordsResult;
import org.apache.kafka.clients.admin.DeletedRecords;
import org.apache.kafka.clients.admin.RecordsToDelete;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class KafkaAdminHandler {
    private static final Logger logger = LoggerFactory.getLogger(KafkaAdminHandler.class);
    private AdminClient adminClient = AdminClient.create(Config.KAFKAADMIN());

    public void deleteRecord(String topicName, int partitionIndex, int beforeIndex){
        try{
            TopicPartition topicPartition = new TopicPartition(topicName, partitionIndex);
            Map<TopicPartition, RecordsToDelete> deleteMap = new HashMap<>();
            deleteMap.put(topicPartition, RecordsToDelete.beforeOffset(beforeIndex + 1));
            adminClient.deleteRecords(deleteMap);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    public void deleteBykey(String topicName, int partitionIndex, int beforeIndex, String key) {
        try{
            TopicPartition topicPartition = new TopicPartition(topicName, partitionIndex);
            Map<TopicPartition, RecordsToDelete> deleteMap = new HashMap<>();
            deleteMap.put(topicPartition, RecordsToDelete.beforeOffset(beforeIndex + 1));
            DeleteRecordsResult result = adminClient.deleteRecords(deleteMap);
            Map<TopicPartition, KafkaFuture<DeletedRecords>> lowWatermarks = result.lowWatermarks();

            try{
                for (Map.Entry<TopicPartition, KafkaFuture<DeletedRecords>> entry : lowWatermarks.entrySet()) {
                    System.out.println(entry.getKey().topic() + " " + entry.getKey().partition() + " " + entry.getValue().get().lowWatermark());
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
