package com.bank.produces.automationproducer.services;

import com.bank.produces.automationproducer.config.Config;
import com.bank.produces.automationproducer.models.Transfer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class ProduceTransfer extends Thread{
    private static final Logger logger = LoggerFactory.getLogger(ProduceTransfer.class);
    private KafkaProducer<String, String> producer;
    private Transfer transfer;

    public ProduceTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    @Override
    public void run() {
        super.run();
        sendData(this.transfer);
    }

    private void sendData(Transfer transfer){
        logger.info("Data Publish on Kafka: "+ transfer.toString());

        producer = new KafkaProducer<String, String>(Config.KafkaConfig());
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(transfer.getUdid(), transfer.getUniqueid(), transfer.toString());
        producer.send(record);
        savetoDB(transfer);
    }

    private void savetoDB(Transfer transfer){
        logger.info("Data save to Database with status Pending...");
        final String uri = "http://"+ Config.SERVICESDB +"/savetodb";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(uri, transfer, String.class);
    }
}
