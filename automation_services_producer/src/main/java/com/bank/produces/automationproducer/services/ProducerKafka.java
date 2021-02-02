package com.bank.produces.automationproducer.services;

import com.bank.produces.automationproducer.models.BankTujuan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerKafka {
    public static String TOPIC = "";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendData(BankTujuan bankTujuan) {
        kafkaTemplate.send(TOPIC, bankTujuan.toString());
    }
}
