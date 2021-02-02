package com.bank.database.automationdatabaseservices.controller;

import com.bank.database.automationdatabaseservices.config.Config;
import com.bank.database.automationdatabaseservices.model.*;
import com.bank.database.automationdatabaseservices.model.auth.UserModel;
import com.bank.database.automationdatabaseservices.repository.DevicesRepository;
import com.bank.database.automationdatabaseservices.repository.TransferBankRepository;
import com.bank.database.automationdatabaseservices.repository.auth.UserRepository;
import com.bank.database.automationdatabaseservices.security.SecurePassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class TransferBankController {
    private static final Logger logger = LoggerFactory.getLogger(TransferBankController.class);

    @Autowired
    private TransferBankRepository transferBankRepository;

    @Autowired
    private DevicesRepository devicesRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/tb/insert")
    public ResponseEntity<Boolean> insert(@RequestBody TransferBank transferBank, HttpServletRequest servletRequest){
        logger.info(servletRequest.getServletPath() + " Access by Producer..");
        try{
            Date date = new Date();
            transferBank.setPassword(SecurePassword.getPassword(transferBank.getPassword(),"bcrypt"));
            transferBank.setMpin(SecurePassword.getPassword(transferBank.getMpin(), "bcrypt"));
            transferBank.setCreateat(date);
            transferBank.setStatus("Pending");
            logger.info(servletRequest.getServletPath() + " Saving to database..");
            transferBankRepository.save(transferBank);
            return ResponseEntity.ok().body(true);
        }catch (Exception ex) {
            logger.error(servletRequest.getServletPath() + " Error:" + ex.getMessage());
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping("/tb/update")
    public ResponseEntity<Object> update(@RequestBody Status status, HttpServletRequest servletRequest){
        try{
            Date date = new Date();
            transferBankRepository.updateData(status.getStatus(), date, status.getKey());
            logger.info(servletRequest.getServletPath()+ " Update: " + status.getKey() + " with message " + status.getStatus());
            return ResponseEntity.ok().body("Updated!");
        } catch (Exception e) {
            logger.error(servletRequest.getServletPath() + " Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error!");
        }
    }

    @GetMapping("/transaction/{uniqueid}")
    public ResponseEntity<Object> getone(@PathVariable String uniqueid, @RequestHeader String apikey, HttpServletRequest servletRequest){
        try{
            UserModel userModel = Config.GET(apikey);
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if (userModel.getUsername().equals(username)){
                TransferBank tb = transferBankRepository.findByKey(uniqueid);
                logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                return ResponseEntity.status(HttpStatus.OK).body(tb);
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

    @GetMapping("/transfer/list")
    public ResponseEntity<Object> getall(@RequestHeader String apikey, HttpServletRequest servletRequest){
        try{
            UserModel userModel = Config.GET(apikey);
            String address = servletRequest.getRemoteAddr();
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            logger.info("Access by "+ apikey.substring(0, 10) + "-" + username + " | via API from: " + address);
            if (userModel.getUsername().equals(username)){
                List<DevicesModel> modifydevice;
                modifydevice = devicesRepository.findAll();

                List<TransferBank> modifydata;
                modifydata = transferBankRepository.findAll();

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
                return ResponseEntity.status(HttpStatus.OK).body(modifydata);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Config.RESPONSECODE(HttpStatus.FORBIDDEN.value(), "Please Login or Check your API Key...", ""));
        }

        return null;
    }

    @GetMapping("/transaction")
    public ResponseEntity<Object> getTransaction(@RequestHeader String apikey, HttpServletRequest servletRequest) {
        try{
            UserModel userModel = Config.GET(apikey);
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if (userModel.getUsername().equals(username)){
                List<DevicesModel> modifydevice;
                modifydevice = devicesRepository.findAll();

                List<TransferBank> modifydata;
                modifydata = transferBankRepository.findAll();

                List<DevicesModel> finalModifydevice = modifydevice;
                modifydata.forEach(trans -> {
                    finalModifydevice.forEach(account -> {
                        if (account.getUdid().equals(trans.getUdid())) {
                            trans.setUsername(account.getName());
                            trans.setRekening("TRF/TO - "+ trans.getRekening());
                        }
                    });
                });
                logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                return ResponseEntity.status(HttpStatus.OK).body(modifydata);
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

    @PostMapping("/transfer")
    public ResponseEntity<Object> transfer(@RequestBody PublishTransfer publish, @RequestHeader String apikey, HttpServletRequest servletRequest) {
        try{
            UserModel userModel = Config.GET(apikey);
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if(userModel.getUsername().equals(username)){
                final String url = "http://" + Config.PUBLISHER + ":" + Config.PUBLISHERPORT + "/transfer/publish";
                DevicesModel devicesModel = devicesRepository.selectOne(publish.getUdid());

                if (devicesModel.getTemplogin() == 1) {
                    logger.error("Account " + devicesModel.getName() + " Blocked by System...");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Config.RESPONSECODE(HttpStatus.FORBIDDEN.value(), "Account " + devicesModel.getName() + " Blocked by System...", null));
                } else if (devicesModel.getTemplogin() == 2) {
                    logger.error("Account " + devicesModel.getName() + " Locked, please contact admin...");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Config.RESPONSECODE(HttpStatus.FORBIDDEN.value(), "Account " + devicesModel.getName() + " Locked, please contact admin...", null));
                }else if (devicesModel.getTemplogin() == 0) {
                    BankTujuan bankTujuan = new BankTujuan();
                    String secret = "automationcrypt";

                    bankTujuan.setUdid(publish.getUdid());
                    bankTujuan.setNominal(publish.getNominal());
                    bankTujuan.setRekening(publish.getRekening());
                    bankTujuan.setBank(devicesModel.getBank());
                    bankTujuan.setUsername(devicesModel.getUsername());
                    bankTujuan.setPassword(devicesModel.getPassword());
                    bankTujuan.setMpin(devicesModel.getMpin());
                    bankTujuan.setPort(devicesModel.getDestination());

                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> data = restTemplate.postForEntity(url, bankTujuan, String.class);
                    String iddata = data.getBody();

                    logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                    return ResponseEntity.status(HttpStatus.OK).body(Config.RESPONSECODE(HttpStatus.OK.value(), "On Proccess..", iddata));
                } else {
                    logger.error("Account " + devicesModel.getName() + " Can't access, Something wrong with our database, please contact Developer or Database administrator...");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Config.RESPONSECODE(HttpStatus.FORBIDDEN.value(), "Account " + devicesModel.getName() + " Can't access, Something wrong with our database, please contact Developer or Database administrator...", null));
                }
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

    @PostMapping("/publish/multi")
    public ResponseEntity<Object> multi(@RequestBody List<Map<String, String>> publish, @RequestHeader String apikey, HttpServletRequest servletRequest){
        UserModel userModel = Config.GET(apikey);
        try{
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if(userModel.getUsername().equals(username)){
                List<Map<String, String>> res = new ArrayList<>();
                publish.forEach((pub) -> {
                    final String url = "http://" + Config.PUBLISHER + ":" + Config.PUBLISHERPORT + "/transfer/publish";
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> data = restTemplate.postForEntity(url, pub, String.class);
                    String iddata = data.getBody();
                    pub.remove("mpin");
                    pub.remove("password");
                    pub.remove("rekening");
                    pub.remove("nominal");
                    pub.remove("port");
                    pub.put("uniqueid", iddata);
                    res.add(pub);
                });
                logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                return ResponseEntity.status(HttpStatus.OK).body(Config.mResponseCode(HttpStatus.OK.value(), res, "Transaction on Proccess..."));
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

    @PostMapping("/totaltrans")
    public ResponseEntity<Object> totaltrans(@RequestBody Map<String, String> form, @RequestHeader String apikey, HttpServletRequest servletRequest) {
        try{
            UserModel userModel = Config.GET(apikey);
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if(userModel.getUsername().equals(username)){
                logger.info(servletRequest.getServletPath() + " Remote Host:" + servletRequest.getRemoteHost() + " User:" + username + " Method:" + servletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + servletRequest.getHeader("User-Agent"));
                return ResponseEntity.status(HttpStatus.OK).body(transferBankRepository.totalTransfer(form.get("udid"), form.get("status")));
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
}
