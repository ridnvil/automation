package com.devices.automation.repository;

import com.devices.automation.config.Config;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public interface APIRepository {

    static void updateStatus(String message, String key) {
        Map<String, String> status = new HashMap<>();
        status.put("key", key);
        status.put("status", message);
        final String uri = "http://"+ Config.SERVICESDB+"/tb/update";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(uri, status, String.class);
    }

    static void blockAccount(boolean status, String udid) {
        final String uri = "http://"+ Config.SERVICESDB+"/block/" + udid;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> ststus = restTemplate.postForEntity(uri, status, String.class);
    }

    static void devicesBusy(String udid, int busy) {
        final String uri = "http://"+ Config.SERVICESDB+"/device/busy";
        Map<String, String> devices = new HashMap<>();
        devices.put("udid", udid);
        devices.put("busy", String.valueOf(busy));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(uri, devices, String.class);
    }
}
