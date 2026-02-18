package com.wallet.wallet_transaction_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionTestController {

    @GetMapping("/transactions/test")
    public String test() {
        return "Transaction Service Working";
    }
}
