package com.wallet.wallet_account_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountTestController {


    @GetMapping("/accounts/test")
    public String test() {
        return "Account Service Working";
    }
}
