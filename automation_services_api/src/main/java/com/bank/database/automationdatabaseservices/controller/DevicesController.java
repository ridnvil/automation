package com.bank.database.automationdatabaseservices.controller;

import com.bank.database.automationdatabaseservices.config.Config;
import com.bank.database.automationdatabaseservices.model.*;
import com.bank.database.automationdatabaseservices.model.auth.UserModel;
import com.bank.database.automationdatabaseservices.repository.DevicesRepository;
import com.bank.database.automationdatabaseservices.repository.TransferBankRepository;
import com.bank.database.automationdatabaseservices.repository.auth.UserRepository;
import com.bank.database.automationdatabaseservices.security.AESAlgoritma;
import com.bank.database.automationdatabaseservices.services.FCMServices;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class DevicesController {
    private static final Logger logger = LoggerFactory.getLogger(DevicesController.class);
    @Autowired
    private DevicesRepository devicesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransferBankRepository transferBankRepository;

    @PostMapping("/devices/register")
    public ResponseEntity input(@RequestBody DevicesModel devicesModel, @RequestHeader String apikey){
        try{
            UserModel userModel = Config.GET(apikey);
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if (userModel.getUsername().equals(username)){
                Date date = new Date();
                devicesModel.setCreatedAt(date);
                devicesModel.setBusy(0);
                devicesModel.setUpdateAt(date);
                String secret = "automationcrypt";

                devicesModel.setPassword(AESAlgoritma.encrypt(devicesModel.getPassword(), secret));
                devicesModel.setMpin(AESAlgoritma.encrypt(devicesModel.getMpin(), secret));

                try {
                    devicesRepository.save(devicesModel);
                }catch (Exception e) {
                    Map<String, String> message = new HashMap<>();
                    message.put("message", "SQL Error: "+ e.getMessage().split(" ")[7] + "/duplicate");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
                }
                return ResponseEntity.status(HttpStatus.OK).body("Saved");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found!");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/devices/update/{udid}")
    public ResponseEntity update(@PathVariable String udid, @RequestBody DevicesModel devicesModel, @RequestHeader String apikey){
        UserModel userModel = Config.GET(apikey);
        try{
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if (userModel.getUsername().equals(username)){
                Date date = new Date();
                devicesModel.setUpdateAt(date);
                String secret = "automationcrypt";
                devicesRepository.update(
                        udid,
                        devicesModel.getName(),
                        devicesModel.getNumber(),
                        AESAlgoritma.encrypt(devicesModel.getPassword(), secret),
                        devicesModel.getUsername(),
                        AESAlgoritma.encrypt(devicesModel.getMpin(), secret),
                        devicesModel.getRekening(),
                        devicesModel.getProvider(),
                        devicesModel.getCredit(),
                        devicesModel.getActive(),
                        devicesModel.getDestination(),
                        devicesModel.getBank(),
                        devicesModel.getUpdateAt());
                return ResponseEntity.status(HttpStatus.OK).body("Saved");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found!");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error from Server..");
        }
    }

    @GetMapping("/devices/{status}")
    public ResponseEntity<Object> getAll(@PathVariable String status, @RequestHeader String apikey, HttpServletRequest servletRequest){
        try{
            UserModel userModel = Config.GET(apikey);
            List<DevicesModel> modelList = new ArrayList<>();
            List<ADBDevicesModel> tempdevice = getDevicesAPI();
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if (userModel.getUsername().equals(username)) {
                if (status.equals("all")) {
                    modelList.clear();
                    tempdevice.forEach((adb) -> {
                        if (!adb.getUdid().contains("offline")) {
                            devicesRepository.updateStatus("ready", adb.getUdid());
                            modelList.add(devicesRepository.selectOne(adb.getUdid()));
                        } else {
                            String udid = adb.getUdid().replace("\toffline", "");
                            devicesRepository.updateStatus("offline", udid);
                            DevicesModel devicesModel = devicesRepository.selectOne(udid);
                            modelList.add(devicesModel);
                        }
                    });
                } else if (status.equals("offline")) {
                    modelList.clear();
                    tempdevice.forEach((adb) -> {
                        if (!adb.getUdid().contains("offline")) {
                            devicesRepository.updateStatus("ready", adb.getUdid());
                        } else {
                            String udid = adb.getUdid().replace("\toffline", "");
                            devicesRepository.updateStatus("offline", udid);
                            modelList.add(devicesRepository.selectOne(udid));
                        }
                    });
                } else if (status.equals("ready")) {
                    modelList.clear();
                    tempdevice.forEach((adb) -> {
                        if (!adb.getUdid().contains("offline")) {
                            devicesRepository.updateStatus("ready", adb.getUdid());
                            modelList.add(devicesRepository.selectOne(adb.getUdid()));
                        } else {
                            String udid = adb.getUdid().replace("\toffline", "");
                            devicesRepository.updateStatus("offline", udid);
                        }
                    });
                } else if(status.equals("allin")) {
                    devicesRepository.findAll().forEach((devices) -> {
                        modelList.add(devices);
                    });
                }else {
                    modelList.clear();
                }
                logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                return ResponseEntity.status(HttpStatus.OK).body(modelList);
            }
        }catch (Exception e){
            logger.error(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " Status:" + HttpStatus.NOT_ACCEPTABLE.value() + " User-Agent:" + servletRequest.getHeader("User-Agent") + " Error: Our system can't allow header request, Please check header request...");
            Map<String, Object> res = new HashMap<>();
            res.put("code", HttpStatus.NOT_ACCEPTABLE.value());
            res.put("method", servletRequest.getMethod());
            res.put("message", "Our system can't allow header request, Please check header request...");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(res);
        }

        return null;
    }

    @GetMapping("/devices")
    public ResponseEntity getReadyDevices(@RequestHeader String apikey, HttpServletRequest servletRequest) {
        try{
            UserModel userModel = Config.GET(apikey);
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if (userModel.getUsername().equals(username)){
                try {
                    List<DevicesModel> list = devicesRepository.showAll();
                    logger.info("Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                    return ResponseEntity.status(HttpStatus.OK).body(list);
                }catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
                }
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found!");
            }
        }catch (Exception e){
            logger.error("Remote Host:" + servletRequest.getRemoteHost() + " Status:" + HttpStatus.NOT_ACCEPTABLE.value() + " Error Catch: Check bellow!!" + " User-Agent:" + servletRequest.getHeader("User-Agent"));
            e.printStackTrace();
            Map<String, String> res = new HashMap<>();
            res.put("code", String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
            res.put("method", servletRequest.getMethod());
            res.put("message", "Our system can't allow header request, Please check header request...");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(res);
        }
    }

    @PostMapping("/masatenggangpulsa")
    public ResponseEntity<String> updateCredit(@RequestBody Map<String, String> credit){
        DevicesModel devicesModel = devicesRepository.selectOne(credit.get("udid"));

        if (devicesModel != null){
            String[] datesplit = devicesModel.getActive().split("-");
            logger.info(String.valueOf(datesplit.length));
            devicesRepository.updateCredit(Double.parseDouble(credit.get("credit")), credit.get("active"), credit.get("udid"));
            logger.info("Account " + devicesModel.getName() + " update to " + credit.get("active") + " expire day with provider " + devicesModel.getProvider());
            FCMServices fcmServices = new FCMServices(devicesModel.getNumber() + " | " + devicesModel.getName(), devicesModel.getProvider() + " | Masa aktif " + credit.get("active"));
            fcmServices.start();
            return ResponseEntity.status(HttpStatus.OK).body("Updated");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UDID not found");
        }
    }

    @PostMapping("/device/busy")
    public ResponseEntity<String> updatebusy(@RequestBody DevicesModel busy){
        try{
            devicesRepository.updateBusy(busy.getBusy(), busy.getUdid());
            return ResponseEntity.status(HttpStatus.OK).body("Devices busy");
        }catch (Exception exp){
            logger.info(exp.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exp.getMessage());
        }
    }

    @GetMapping("/block/{udid}")
    public ResponseEntity<String> blockdevices(@PathVariable String udid) {
        try{
            devicesRepository.blockDevices(udid);
            DevicesModel devicesModel = devicesRepository.selectOne(udid);
            FCMServices fcmServices = new FCMServices("Account Blocked!", "Account " + devicesModel.getName() + "|" + devicesModel.getBank().toUpperCase() + " blocked");
            fcmServices.start();
            logger.info("Opss, This account with Name "+ devicesModel.getName() +" & UDID "+ udid +" has been blocked by system because failed to login, please contact admin account...");
            return ResponseEntity.status(HttpStatus.OK).body("Devices Locked!");
        }catch (Exception ex) {
            logger.info(ex.getMessage());
        }
        return null;
    }

    @GetMapping("/offdevice/{udid}")
    public ResponseEntity<String> offDevices(@PathVariable String udid) {
        try{
            devicesRepository.offDevices(udid);
            DevicesModel devicesModel = devicesRepository.selectOne(udid);
            FCMServices fcmServices = new FCMServices("Account Disconnected!", "Account " + devicesModel.getName() + "|" + devicesModel.getBank().toUpperCase() + " Disconnected");
            fcmServices.start();
            logger.info("Opss, This account with Name "+ devicesModel.getName() +" & UDID "+ udid +" Disconnect by admin...");
            return ResponseEntity.status(HttpStatus.OK).body("Devices Disconnected!");
        }catch (Exception ex) {
            logger.info(ex.getMessage());
        }
        return null;
    }

    @GetMapping("/openblock/{udid}")
    public ResponseEntity<String> openblockdevices(@PathVariable String udid) {
        try{
            devicesRepository.openblockDevices(udid);
            DevicesModel devicesModel = devicesRepository.selectOne(udid);
            logger.info("Account Name "+ devicesModel.getName() +"|"+ udid +" ready to use..");
            return ResponseEntity.status(HttpStatus.OK).body("Devices Ready!");
        }catch (Exception ex) {
            logger.info(ex.getMessage());
        }
        return null;
    }

    @GetMapping("/device/{udid}")
    public ResponseEntity<Object> port(@PathVariable String udid){
        try{
            DevicesModel devicesModel = devicesRepository.selectOne(udid);
            devicesModel.setUsername(devicesModel.getUsername().substring(0, 3) + "***");
            devicesModel.setPassword(devicesModel.getPassword().substring(0, 3) + "***");
            return ResponseEntity.status(HttpStatus.OK).body(devicesModel);
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/device/queue/{udid}")
    public ResponseEntity<?> queue(@PathVariable String udid, @RequestHeader String apikey, HttpServletRequest servletRequest) {
        try{
            UserModel userModel = Config.GET(apikey);
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if (userModel.getUsername().equals(username)){
                try {
                    List<TransferBank> list = transferBankRepository.findByUdid(udid, "Pending");
                    logger.info("Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                    return ResponseEntity.status(HttpStatus.OK).body(list);
                }catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
                }
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found!");
            }
        }catch (Exception e){
            logger.error("Remote Host:" + servletRequest.getRemoteHost() + " Status:" + HttpStatus.NOT_ACCEPTABLE.value() + " Error Catch: Check bellow!!" + " User-Agent:" + servletRequest.getHeader("User-Agent"));
            e.printStackTrace();
            Map<String, String> res = new HashMap<>();
            res.put("code", String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()));
            res.put("method", servletRequest.getMethod());
            res.put("message", "Our system can't allow header request, Please check header request...");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(res);
        }
    }

    private List<ADBDevicesModel> getDevicesAPI(){
        List<ADBDevicesModel> devices = new ArrayList<>();

        final String url = "http://"+ Config.DEVICESMONITORINGSERVER + ":" + Config.DEVICESMONITORINGPORT + "/check";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> datadevices = restTemplate.getForEntity(url, String.class);
        Gson gson = new Gson();
        ADBDevicesModel[] adbDevicesModels = gson.fromJson(datadevices.getBody(), ADBDevicesModel[].class);

        for (int i =0; i < adbDevicesModels.length; i++){
            if(!adbDevicesModels[i].equals("") && adbDevicesModels[i] != null){
                devices.add(adbDevicesModels[i]);
            }
        }

        return devices;
    }
}
