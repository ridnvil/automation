package com.bank.produces.automationproducer.controller;

import com.bank.produces.automationproducer.models.Transfer;
import com.bank.produces.automationproducer.services.ProduceTransfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TransferController {

    @PostMapping("/trans")
    public ResponseEntity<?> transfer(@RequestBody Transfer transfer) {
        UUID keydata = UUID.randomUUID();
        transfer.setUniqueid(keydata.toString());
        ProduceTransfer produceTransfer = new ProduceTransfer(transfer);
        produceTransfer.start();
        return ResponseEntity.status(HttpStatus.OK).body(transfer);
    }
}
