package com.bank.database.automationdatabaseservices.controller;

import com.bank.database.automationdatabaseservices.config.Config;
import com.bank.database.automationdatabaseservices.model.Transfer;
import com.bank.database.automationdatabaseservices.model.TransferModel;
import com.bank.database.automationdatabaseservices.model.auth.UserModel;
import com.bank.database.automationdatabaseservices.repository.DevicesRepository;
import com.bank.database.automationdatabaseservices.repository.TransferRepository;
import com.bank.database.automationdatabaseservices.repository.auth.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// New method to handle single transfer & multi transfer
@RestController
public class WithdrawController {
    private static final Logger logger = LoggerFactory.getLogger(WithdrawController.class);

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private DevicesRepository devicesRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/trans")
    public ResponseEntity<?> transfer(@RequestBody TransferModel transferModel, @RequestHeader String apikey, HttpServletRequest httpServletRequest) {
        try{
            UserModel userModel = Config.GET(apikey);
            assert userModel != null;
            String username = userRepository.login(userModel.getUsername()).getUsername();
            if (userModel.getUsername().equals(username)) {
                String udid = transferModel.getUdid();
                transferModel.getTujuan().forEach(data -> {
                    // hold method... try to handle on automation core.
                });
                logger.info(httpServletRequest.getServletPath() + " Remote Host:" + httpServletRequest.getRemoteHost() + " User:" + username + " Method:" + httpServletRequest.getMethod() + " Status:" + HttpStatus.OK.value() + " User-Agent:" + httpServletRequest.getHeader("User-Agent"));
                return ResponseEntity.status(HttpStatus.OK).body(transferModel);
            }
        }catch (Exception e) {
            logger.error(httpServletRequest.getServletPath() + " Remote Host:" + httpServletRequest.getRemoteHost() + " Status:" + HttpStatus.NOT_ACCEPTABLE.value() + " User-Agent:" + httpServletRequest.getHeader("User-Agent") + " Error: Our system can't allow header request, Please check header request...");
            Map<String, Object> res = new HashMap<>();
            res.put("code", HttpStatus.NOT_ACCEPTABLE.value());
            res.put("method", httpServletRequest.getMethod());
            res.put("message", "Our system can't allow header request, Please check header request...");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(res);
        }

        return null;
    }

    @PostMapping("/savetodb")
    public ResponseEntity<Boolean> saveDB(@RequestBody TransferModel transferModel) {
        try{
            final int[] index = {0};
            transferModel.getTujuan().forEach(tujuan -> {
                Transfer transfer = new Transfer();
                transfer.setUniqueid(transferModel.getUniqueid());
                transfer.setUdid(transferModel.getUdid());
                transfer.setBank(transferModel.getBank());
                transfer.setIndex(index[0]);
                transfer.setRekening(tujuan.getRekening());
                transfer.setNominal(tujuan.getNominal());
                transfer.setStatus(tujuan.getStatus());
                transfer.setCreated(new Date());
                transfer.setUpdate(new Date());
                transferRepository.save(transfer);
                index[0]++;
            });
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body(false);
        }
    }
}
