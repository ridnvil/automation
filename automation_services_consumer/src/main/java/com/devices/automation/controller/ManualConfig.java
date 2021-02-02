package com.devices.automation.controller;

import com.devices.automation.config.Config;
import com.devices.automation.services.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ManualConfig {
    private static Logger logger = LoggerFactory.getLogger(ManualConfig.class);

    @PostMapping("/config/{udid}")
    public ResponseEntity config(@RequestBody Map<String,String> config){
        Config.KAFKAHOST = config.get("kafka");
        Config.SERVICESDB = config.get("servicedb");
        Config.APPIUMSERVER = config.get("appium");
        Config.KAFKATOPIC = config.get("topic");
        logger.info("Kafka now running on " + config.get("kafka") + " and listen to topic: "+ config.get("topic") +", Access Service on " + config.get("servicedb") + ", Running automation on " + config.get("appium"));
        return ResponseEntity.status(HttpStatus.OK).body("Config Success");
    }
}
