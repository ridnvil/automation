package com.bank.produces.automationproducer.services;

import com.bank.produces.automationproducer.models.BankTujuan;
import com.bank.produces.automationproducer.config.Config;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class BankProducer extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(BankProducer.class);
    private KafkaProducer<String, String> producer;
    private BankTujuan thisbankTujuan;
    private String thiskeydata;

    public BankProducer(BankTujuan bankTujuan, String keydata){
        this.thisbankTujuan = bankTujuan;
        this.thiskeydata = keydata;
    }

    @Override
    public void run() {
        super.run();
        sendData(this.thisbankTujuan, this.thiskeydata);
    }

    private void sendData(BankTujuan bankTujuan, String keydata){
        logger.info("Data Publish on Kafka: "+ bankTujuan.toString());

        producer = new KafkaProducer<String, String>(Config.KafkaConfig());
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(bankTujuan.getUdid(), keydata, bankTujuan.toString());
        producer.send(record);
        savetoDB(bankTujuan, keydata);
    }

    private void abortKafkaProducer(){
        producer.abortTransaction();
    }

    private void savetoDB(BankTujuan bankTujuan, String key){
        logger.info("Data save to Database with status Pending...");
        final String uri = "http://"+ Config.SERVICESDB +"/tb/insert";
        RestTemplate restTemplate = new RestTemplate();
        bankTujuan.setUniqueid(key);
        restTemplate.postForEntity(uri, bankTujuan, String.class);
    }
}
