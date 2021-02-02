package com.devices.automation.services;

import com.devices.automation.automation.Automation;
import com.devices.automation.config.Config;
import com.devices.automation.core.CoreBank;
import com.devices.automation.model.BankTujuan;
import com.devices.automation.model.Transfer;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;

public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
    private Config config;

    public void consume() {
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(Config.KAFKACONFIG("transfer"));
        kafkaConsumer.subscribe(Arrays.asList(config.KAFKATOPIC));
        KafkaAdminHandler kafkaAdminHandler = new KafkaAdminHandler();

        while (true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records){
                Gson g = new Gson();
//                BankTujuan bankTujuan = g.fromJson(record.value(), BankTujuan.class);
                System.out.println(record.value());
                Transfer transfer = g.fromJson(record.value(), Transfer.class);
                try {
//                    Automation automation = new Automation();
//                    automation.setup(bankTujuan, record.key());
                    new CoreBank(transfer);
                    Thread.sleep(30000);
                    kafkaAdminHandler.deleteRecord(transfer.getUdid(), record.partition(), Math.toIntExact((record.offset())));
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }
        }
    }
}
