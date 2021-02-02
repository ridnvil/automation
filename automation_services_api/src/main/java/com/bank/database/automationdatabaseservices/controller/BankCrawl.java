package com.bank.database.automationdatabaseservices.controller;

import com.bank.database.automationdatabaseservices.config.Config;
import com.bank.database.automationdatabaseservices.model.BankCrawlParamModel;
import com.bank.database.automationdatabaseservices.model.DataBankModel;
import com.bank.database.automationdatabaseservices.model.auth.UserModel;
import com.bank.database.automationdatabaseservices.repository.auth.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;

@RestController
public class BankCrawl {
    private static final Logger logger = LoggerFactory.getLogger(BankCrawl.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/v0.2/mutasi/{bank}")
    public ResponseEntity<?> getmutasi(@PathVariable String bank, @RequestHeader String apikey, @RequestBody BankCrawlParamModel param, HttpServletRequest servletRequest) {
        UserModel userModel = Config.GET(apikey);
        try{
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if(userModel.getUsername().equals(username)) {
                String url = "http://"+Config.CRAWLAPIURL+":"+Config.CRAWLAPIURLPORT+"/mutasi-" + bank;
                RestTemplate restTemplate = new RestTemplate();
                HttpEntity<BankCrawlParamModel> entity = new HttpEntity<>(param, null);
                ResponseEntity<DataBankModel> data = restTemplate.postForEntity(url, param, DataBankModel.class);
                logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                return ResponseEntity.status(HttpStatus.OK).body(data.getBody());
            }
        }catch (HttpClientErrorException e){
            logger.error(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " Status:" + HttpStatus.NOT_ACCEPTABLE.value() + " User-Agent:" + servletRequest.getHeader("User-Agent") + " Error: Our system can't allow header request, Please check header request...");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Config.RESPONSECODE(HttpStatus.NOT_ACCEPTABLE.value(), "Error: " + e.getMessage(), ""));
        }

        return null;
    }

    @GetMapping("/accounts")
    public ResponseEntity getAccount(@RequestHeader String apikey, HttpServletRequest servletRequest) {
        try{
            UserModel userModel = Config.GET(apikey);
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if(userModel.getUsername().equals(username)) {
                String url = "http://"+Config.CRAWLAPIURL+":"+Config.CRAWLAPIURLPORT+"/accounts";
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Object> data = restTemplate.postForEntity(url, null, Object.class);
                logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                return ResponseEntity.status(HttpStatus.OK).body(data.getBody());
            }
        }catch (HttpClientErrorException e){
            logger.error(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " Status:" + HttpStatus.NOT_ACCEPTABLE.value() + " User-Agent:" + servletRequest.getHeader("User-Agent") + " Error: Our system can't allow header request, Please check header request...");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Config.RESPONSECODE(HttpStatus.NOT_ACCEPTABLE.value(), "Error: " + e.getMessage(), ""));
        }

        return null;
    }

    @PostMapping("/trigger")
    public ResponseEntity triggerOnOff(@RequestHeader String apikey, @RequestBody Map<String, String> param, HttpServletRequest servletRequest) {
        try{
            UserModel userModel = Config.GET(apikey);
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if(userModel.getUsername().equals(username)) {
                String url = "http://"+Config.CRAWLAPIURL+":"+Config.CRAWLAPIURLPORT+"/trigger";
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Object> data = restTemplate.postForEntity(url, param, Object.class);
                logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                return ResponseEntity.status(HttpStatus.OK).body(data.getBody());
            }
        }catch (HttpClientErrorException e){
            logger.error(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " Status:" + HttpStatus.NOT_ACCEPTABLE.value() + " User-Agent:" + servletRequest.getHeader("User-Agent") + " Error: Our system can't allow header request, Please check header request...");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Config.RESPONSECODE(HttpStatus.NOT_ACCEPTABLE.value(), "Error: " + e.getMessage(), ""));
        }

        return null;
    }

    @GetMapping("/api/viewInquiry")
    public ResponseEntity viewInquiry(@RequestBody Map<String, String> request, HttpServletRequest servletRequest) {
//        try{
//            UserModel userModel = Config.GET(apikey);
//            assert userModel != null;
//            String username = userRepository.login(userModel.getUsername()).getUsername();
//            if(userModel.getUsername().equals(username)) {
//                String url = "http://"+Config.CRAWLAPIURL+":"+Config.CRAWLWATERFALLPORT+"/api/viewInquiry";
//                RestTemplate restTemplate = new RestTemplate();
//                HttpHeaders header = new HttpHeaders();
//                header.setContentType(MediaType.APPLICATION_JSON);
//                header.remove("apikey");
//                HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, header);
//                ResponseEntity<Object> data = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
//                logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
//                return ResponseEntity.status(HttpStatus.OK).body(data.getBody());
//            }
//        }catch (HttpClientErrorException e){
//            logger.error(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " Status:" + HttpStatus.NOT_ACCEPTABLE.value() + " User-Agent:" + servletRequest.getHeader("User-Agent") + " Error: Our system can't allow header request, Please check header request...");
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Config.RESPONSECODE(HttpStatus.NOT_ACCEPTABLE.value(), "Error: " + e.getMessage(), ""));
//        }
//
//        return null;
        return ResponseEntity.status(HttpStatus.OK).body(tryGetDataInquiry(request));
    }

    private Map<String, Object> tryGetDataInquiry(Map<String, String> data) {
        String url = "http://"+Config.CRAWLAPIURL+":"+Config.CRAWLWATERFALLPORT+"/api/viewInquiry";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(data, header);
        System.out.println(entity);
        ResponseEntity<Map> datax = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return datax.getBody();
    }
}
