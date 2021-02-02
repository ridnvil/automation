package com.bank.database.automationdatabaseservices.controller;

import com.bank.database.automationdatabaseservices.config.Config;
import com.bank.database.automationdatabaseservices.model.ConfigModels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class DBConfigController {
    private static Logger logger = LoggerFactory.getLogger(DBConfigController.class);

    @PostMapping("/allconfig")
    public ResponseEntity config(@RequestBody ConfigModels conf, HttpServletRequest request){
        String address = request.getRemoteAddr();
        logger.info("Configure by Admin from: "+ address);
        try{
            Config.DBHOST = conf.getDbhost();
            Config.DBNAME = conf.getDbname();
            Config.DBUSERNAME = conf.getDbusername();
            Config.DBPASSWORD = conf.getDbpassword();
            Config.DBPORT = conf.getDbport();
            Config.DBSERVICEHOST = conf.getDbservicehost();
            Config.DBSERVICEPORT = conf.getDbserviceport();
            Config.REDIS = conf.getRedis();
            Config.REDISPORT = conf.getRedisport();
            Config.PUBLISHER = conf.getPublisher();
            Config.PUBLISHERPORT = conf.getPublisherport();
            Config.SUBSCRIBER = conf.getSubscriber();
            Config.SUBSCRIBEPORT = conf.getSubscribeport();
            Config.KAFKAHOST = conf.getKafkahost();
            Config.KAFKAPORT = conf.getKafkaport();
            Config.APPIUMSERVER = conf.getAppiumserver();
            Config.APPIUMPORT = conf.getAppiumport();
            Config.DEVICESMONITORINGSERVER = conf.getDevicesmonitoring();
            Config.DEVICESMONITORINGPORT = conf.getDevicesmonitoringport();
            Config.CRAWLAPIURL = conf.getCrawlurl();
            Config.CRAWLAPIURLPORT = conf.getCrawlurlport();
            Config.CRAWLWATERFALLPORT = conf.getCrawlwaterfallport();
//            Config.success = true;
            logger.info("Success Configured!");
            return ResponseEntity.status(HttpStatus.OK).body("Success Configured!");
        }catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @GetMapping("/showconfig")
    public ResponseEntity show(){
        ConfigModels config = new ConfigModels();
        config.setDbname(Config.DBNAME);
        config.setDbusername(Config.DBUSERNAME);
        config.setDbhost(Config.DBHOST);
        config.setDbpassword(Config.DBPASSWORD);
        config.setDbport(Config.DBPORT);
        config.setDbservicehost(Config.DBSERVICEHOST);
        config.setDbserviceport(Config.DBSERVICEPORT);
        config.setRedisport(Config.REDISPORT);
        config.setRedis(Config.REDIS);
        config.setPublisher(Config.PUBLISHER);
        config.setPublisherport(Config.PUBLISHERPORT);
        config.setSubscriber(Config.SUBSCRIBER);
        config.setSubscribeport(Config.SUBSCRIBEPORT);
        config.setKafkahost(Config.KAFKAHOST);
        config.setKafkaport(Config.KAFKAPORT);
        config.setAppiumserver(Config.APPIUMSERVER);
        config.setAppiumport(Config.APPIUMPORT);
        config.setDevicesmonitoring(Config.DEVICESMONITORINGSERVER);
        config.setDevicesmonitoringport(Config.DEVICESMONITORINGPORT);
        config.setCrawlurl(Config.CRAWLAPIURL);
        config.setCrawlurlport(Config.CRAWLAPIURLPORT);
        config.setCrawlwaterfallport(Config.CRAWLWATERFALLPORT);
        return ResponseEntity.status(HttpStatus.OK).body(config);
    }

    @PostMapping("/redisconfig")
    public ResponseEntity redisConfig(@RequestBody Map<String, String> config) {
        try {
            logger.info("Config Redis Succes...");
            Config.REDIS = config.get("host");
            Config.REDISPORT = Integer.parseInt(config.get("port"));
            return ResponseEntity.status(HttpStatus.OK).body("Saved");
        }catch (Exception ex) {
            logger.info("Config Redis Failed...");
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Failed");
        }
    }

    @PostMapping("/add/token")
    public ResponseEntity addToken(@RequestBody Map<String, String> body) {
        Config.userToken.add(body.get("token"));
        return ResponseEntity.status(HttpStatus.OK).body("Added!");
    }
}
