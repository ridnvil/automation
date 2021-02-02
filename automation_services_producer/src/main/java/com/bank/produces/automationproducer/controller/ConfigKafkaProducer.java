package com.bank.produces.automationproducer.controller;

import com.bank.produces.automationproducer.config.Config;
import com.bank.produces.automationproducer.models.KafkaProducerModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConfigKafkaProducer {

    @PostMapping("/publish/config")
    public ResponseEntity kafkaproducer(@RequestBody KafkaProducerModel kafkaProducerModel){
        Config.KAFKAHOST = kafkaProducerModel.getHost() + ":" + kafkaProducerModel.getPort();
        Config.SERVICESDB = kafkaProducerModel.getDbservicehost()+":"+ kafkaProducerModel.getDbserviceport();
        System.out.println(Config.KAFKAHOST);

        return ResponseEntity.ok().body("Publish Config Saved");
    }

    @GetMapping("/publish/start")
    public ResponseEntity start(){
        return ResponseEntity.status(HttpStatus.OK).body("Publisher Running");
    }
}
