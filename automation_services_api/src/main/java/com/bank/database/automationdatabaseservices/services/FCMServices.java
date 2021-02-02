package com.bank.database.automationdatabaseservices.services;

import com.bank.database.automationdatabaseservices.config.Config;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FCMServices extends Thread{
    private String title;
    private String content;

    public FCMServices(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public void run() {
        pushNotification(this.title, this.content);
        super.run();
    }

    private void pushNotification(String title, String content) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            Map<String, Object> data = new HashMap<>();

            headers.set("Authorization", "key=" + Config.firebaseTokenAuth);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            Config.userToken.forEach(token -> {
                Map<String, String> message = new HashMap<>();
                message.put("title", title);
                message.put("message", content);
                data.put("to", token);
                data.put("data", message);
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);
                restTemplate.exchange(Config.firebaseUrl, HttpMethod.POST, entity, String.class);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
