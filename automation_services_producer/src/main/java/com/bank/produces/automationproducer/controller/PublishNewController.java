package com.bank.produces.automationproducer.controller;

import com.bank.produces.automationproducer.models.BankTujuan;
import com.bank.produces.automationproducer.services.ProducerNewKafka;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PublishNewController {

    @PostMapping("/pub/topic")
    public ResponseEntity publish(@RequestBody BankTujuan bankTujuan) {
        UUID keydata = UUID.randomUUID();
        ProducerNewKafka producerNewKafka = new ProducerNewKafka(bankTujuan, keydata.toString());
        producerNewKafka.start();
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
