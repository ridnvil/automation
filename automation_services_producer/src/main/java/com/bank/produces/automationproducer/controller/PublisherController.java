package com.bank.produces.automationproducer.controller;

import com.bank.produces.automationproducer.models.BankTujuan;
import com.bank.produces.automationproducer.services.ProducerKafka;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublisherController {

    private ProducerKafka producerKafka;

    @PostMapping("/pub/data")
    public ResponseEntity publish(@RequestBody BankTujuan bankTujuan) {
        producerKafka.sendData(bankTujuan);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
