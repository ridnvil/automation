package com.bank.produces.automationproducer.controller;

import com.bank.produces.automationproducer.models.BankTujuan;
import com.bank.produces.automationproducer.services.BankProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ProducerController {

    @Autowired
    private RedisTemplate template;

    private static final Logger logger = LoggerFactory.getLogger(ProducerController.class);

    @PostMapping("/transfer/publish")
    public ResponseEntity producer(@RequestBody BankTujuan bankTujuan) {
        logger.info("Publish Data..");
        try{
            UUID keydata = UUID.randomUUID();
            BankProducer bankProducer = new BankProducer(bankTujuan, keydata.toString());
            try{
                template.convertAndSend(bankTujuan.getUdid(), bankTujuan.toString());
                bankProducer.start();
            }catch (Exception e){
                bankProducer.interrupt();
            }

            return ResponseEntity.status(HttpStatus.OK).body(keydata.toString());
        }catch (Exception exp){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }
    }

    @PostMapping("/multi/publish")
    public ResponseEntity multi(@RequestBody List<BankTujuan> bankTujuan){
        List<String> listID = new ArrayList<>();

        try{
            bankTujuan.forEach((bank) -> {
                UUID key = UUID.randomUUID();
                BankProducer bankProducer = new BankProducer(bank, key.toString());
                try{
                    bankProducer.start();
                }catch (Exception e){
                    bankProducer.interrupt();
                }
                listID.add(key.toString());
            });

            return ResponseEntity.status(HttpStatus.OK).body(listID);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/boom/{number}")
    public ResponseEntity boomPost(@RequestBody BankTujuan bankTujuan, @PathVariable int number) {
        List<BankTujuan> bankTujuanList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            bankTujuanList.add(bankTujuan);
        }

        bankTujuanList.forEach(bank -> {
            UUID key = UUID.randomUUID();
            BankProducer bankProducer = new BankProducer(bank, key.toString());
            bankProducer.start();
        });
        return ResponseEntity.status(HttpStatus.OK).body("Running..");
    }
}
