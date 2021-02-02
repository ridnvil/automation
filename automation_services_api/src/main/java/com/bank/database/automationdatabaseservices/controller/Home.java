package com.bank.database.automationdatabaseservices.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Home {
    private static final Logger logger = LoggerFactory.getLogger(Home.class);

    @GetMapping("/")
    public ResponseEntity<Object> home(HttpServletRequest httpServletRequest) {
        Map<String, Object> response = new HashMap<>();
        response.put("Code", HttpStatus.OK.value());
        response.put("Message", "Welcome to bank Automation API..");
        response.put("User", httpServletRequest.getRemoteUser() == null ? "Anonymous": httpServletRequest.getRemoteUser());
        response.put("Remotehost", httpServletRequest.getRemoteHost());
        response.put("Agent", httpServletRequest.getHeader("User-Agent"));
        response.put("Developers", "A2Z Developers Team(Intama)");
        response.put("Contact", "support@azsolusindo.info, sales@intama.info");
        response.put("Address", "JL Lingkar Luar Barat No 23 B-C,Cengkareng - Jakarta Barat Jakarta 11750 INDONESIA");
        return ResponseEntity.ok().body(response);
    }
}
