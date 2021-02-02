package com.bank.database.automationdatabaseservices.controller;

import com.bank.database.automationdatabaseservices.config.Config;
import com.bank.database.automationdatabaseservices.model.DevicesModel;
import com.bank.database.automationdatabaseservices.model.TransferBank;
import com.bank.database.automationdatabaseservices.model.auth.UserModel;
import com.bank.database.automationdatabaseservices.repository.DevicesRepository;
import com.bank.database.automationdatabaseservices.repository.TransferBankRepository;
import com.bank.database.automationdatabaseservices.repository.auth.UserRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DasboardTransferController {
    private static final Logger logger = LoggerFactory.getLogger(DasboardTransferController.class);
    @Autowired
    private DevicesRepository devicesRepository;

    @Autowired
    private TransferBankRepository transferBankRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/trf")
    public ResponseEntity<Object> transfer(@RequestBody Map<String, String> publish, @RequestHeader String apikey, HttpServletRequest servletRequest){
        UserModel userModel = Config.GET(apikey);
        try{
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if(userModel.getUsername().equals(username)){
                final String url = "http://" + Config.PUBLISHER + ":" + Config.PUBLISHERPORT + "/transfer/publish";
                DevicesModel devicesModel = devicesRepository.selectOne(publish.get("udid"));

                if (devicesModel.getTemplogin() == 1) {
                    logger.error("Account " + devicesModel.getName() + " Blocked by System..");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Config.RESPONSECODE(HttpStatus.FORBIDDEN.value(), "Account " + devicesModel.getName() + " Blocked by System..", null));
                } else if (devicesModel.getTemplogin() == 2) {
                    logger.error("Account " + devicesModel.getName() + " Disconnected, please contact admin...");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Config.RESPONSECODE(HttpStatus.FORBIDDEN.value(), "Account Blocked by System...", null));
                } else if (devicesModel.getTemplogin() == 0) {
                    publish.put("bank", devicesModel.getBank());
                    publish.put("username", devicesModel.getUsername());
                    publish.put("password", devicesModel.getPassword());
                    publish.put("mpin", devicesModel.getMpin());
                    publish.put("port", String.valueOf(devicesModel.getDestination()));

                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> data = restTemplate.postForEntity(url, publish, String.class);
                    String iddata = data.getBody();
                    logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                    return ResponseEntity.status(HttpStatus.OK).body(Config.RESPONSECODE(HttpStatus.OK.value(), "On Proccess..", iddata));
                } else {
                    logger.error("Account " + devicesModel.getName() + " Can't access, Something wrong with our database, please contact Developer or Database administrator...");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Config.RESPONSECODE(HttpStatus.FORBIDDEN.value(), "Account Blocked by System...", null));
                }
            }
        }catch (Exception e){
            logger.error(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " Status:" + HttpStatus.NOT_ACCEPTABLE.value() + " User-Agent:" + servletRequest.getHeader("User-Agent") + " Error: Our system can't allow header request, Please check header request...");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Config.RESPONSECODE(HttpStatus.NOT_ACCEPTABLE.value(), "Please Login or Check your API Key...", ""));
        }

        return null;
    }

    @PostMapping("/trf/multi")
    public ResponseEntity<Object> multi(@RequestHeader String apikey, @RequestBody List<Map<String, String>> datapublish, HttpServletRequest servletRequest) {
        UserModel userModel = Config.GET(apikey);
        try{
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if(userModel.getUsername().equals(username)){
                final String url = "http://" + Config.PUBLISHER + ":" + Config.PUBLISHERPORT + "/transfer/publish";
                List<Map<String, String>> dataresponseAPI = new ArrayList<>();
                datapublish.forEach(publish -> {
                    Map<String, String> dataresponse = new HashMap<>();
                    DevicesModel devicesModel = devicesRepository.selectOne(publish.get("udid"));
                    String status = "";
                    String niqueid = "";

                    if (devicesModel.getTemplogin() == 1) {
                        logger.error("Account " + devicesModel.getName() + " Blocked by System..");
                        status = "Account " +  devicesModel.getName() + " Blocked by System..";
                    } else if (devicesModel.getTemplogin() == 2) {
                        logger.error("Account " + devicesModel.getName() + " Disconnected, please contact admin...");
                        status = "Account " + devicesModel.getName() + " Disconnected, please contact admin...";
                    } else if (devicesModel.getTemplogin() == 0) {
                        publish.put("bank", devicesModel.getBank());
                        publish.put("username", devicesModel.getUsername());
                        publish.put("password", devicesModel.getPassword());
                        publish.put("mpin", devicesModel.getMpin());
                        publish.put("port", String.valueOf(devicesModel.getDestination()));

                        logger.info(devicesModel.toString());

                        RestTemplate restTemplate = new RestTemplate();
                        ResponseEntity<String> data = restTemplate.postForEntity(url, publish, String.class);
                        String iddata = data.getBody();
                        niqueid = iddata;
                        status = "Proccess";
                    } else {
                        logger.error("Account " + devicesModel.getName() + " Can't access, Something wrong with our database, please contact Developer or Database administrator...");
                        status = "Problem Account";
                    }

                    dataresponse.put("uniqueid", niqueid);
                    dataresponse.put("account", devicesModel.getName());
                    dataresponse.put("message", status);

                    dataresponseAPI.add(dataresponse);
                });

                logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                return ResponseEntity.status(HttpStatus.OK).body(dataresponseAPI);
            }
        }catch (Exception e){
            logger.error(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " Status:" + HttpStatus.NOT_ACCEPTABLE.value() + " User-Agent:" + servletRequest.getHeader("User-Agent") + " Error: Our system can't allow header request, Please check header request...");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Config.RESPONSECODE(HttpStatus.NOT_ACCEPTABLE.value(), "Please Login or Check your API Key...", ""));
        }

        return null;
    }

    @GetMapping("/trf/{uniqueid}")
    public ResponseEntity<Object> status(@PathVariable String uniqueid, @RequestHeader String apikey, HttpServletRequest servletRequest) {
        UserModel userModel = Config.GET(apikey);
        try{
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if(userModel.getUsername().equals(username)){
                TransferBank transferBank = transferBankRepository.findByKey(uniqueid);
                transferBank.setPassword(transferBank.getPassword().replace(transferBank.getPassword(), "********"));
                transferBank.setUsername(transferBank.getUsername().replace(transferBank.getUsername(), "********"));
                transferBank.setMpin(transferBank.getMpin().replace(transferBank.getMpin(), "********"));
                logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                return ResponseEntity.status(HttpStatus.OK).body(transferBank);
            }
        }catch (Exception e){
            logger.error(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " Status:" + HttpStatus.NOT_ACCEPTABLE.value() + " User-Agent:" + servletRequest.getHeader("User-Agent") + " Error: Our system can't allow header request, Please check header request...");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Config.RESPONSECODE(HttpStatus.NOT_ACCEPTABLE.value(), "Please Login or Check your API Key...", ""));
        }

        return null;
    }

    @GetMapping("/queue")
    public ResponseEntity<Object> queue(@RequestHeader String apikey, HttpServletRequest servletRequest){
        UserModel userModel = Config.GET(apikey);
        String address = servletRequest.getRemoteAddr();
        try{
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if(userModel.getUsername().equals(username)){
                final String url = "http://" + Config.SUBSCRIBER + ":" + Config.SUBSCRIBEPORT + "/queuelist";
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                Gson gson = new Gson();
                List<Map<String, Map<String, String>>> data = gson.fromJson(String.valueOf(response.getBody()), List.class);
                logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                return ResponseEntity.status(HttpStatus.OK).body(data);
            }
        }catch (Exception e){
            logger.error(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " Status:" + HttpStatus.NOT_ACCEPTABLE.value() + " User-Agent:" + servletRequest.getHeader("User-Agent") + " Error: Our system can't allow header request, Please check header request...");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Config.RESPONSECODE(HttpStatus.NOT_ACCEPTABLE.value(), "Please Login or Check your API Key...", ""));
        }

        return null;
    }

    @GetMapping("/trf/list")
    public ResponseEntity<Object> getall(@RequestHeader String apikey, HttpServletRequest servletRequest){
        UserModel userModel = Config.GET(apikey);
        try{
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if (userModel.getUsername().equals(username)){
                List<DevicesModel> modifydevice = new ArrayList<>();
                modifydevice = devicesRepository.findAll();

                List<TransferBank> modifydata = new ArrayList<>();
                modifydata = transferBankRepository.findAllByOrderDate();

                List<DevicesModel> finalModifydevice = modifydevice;
                modifydata.forEach(trans -> {
                    finalModifydevice.forEach(account -> {
                        if (account.getUdid().equals(trans.getUdid())) {
                            trans.setBank(trans.getBank().toUpperCase());
                            trans.setUsername(account.getName());
                            trans.setRekening("TRSF AUTOMATION (DB) TO "+ trans.getRekening());
                        }
                    });
                });

                logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                return ResponseEntity.status(HttpStatus.OK).body(modifydata);
            }
        }catch (Exception e){
            logger.error(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " Status:" + HttpStatus.NOT_ACCEPTABLE.value() + " User-Agent:" + servletRequest.getHeader("User-Agent") + " Error: Our system can't allow header request, Please check header request...");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Config.RESPONSECODE(HttpStatus.FORBIDDEN.value(), "Please Login or Check your API Key...", ""));
        }

        return null;
    }
}
