package com.wallet.wallet_transaction_service.controller;

import com.wallet.wallet_transaction_service.client.AccountClient;
import com.wallet.wallet_transaction_service.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TestController {

    @Autowired
    private AccountClient accountClient;
    @Autowired
    private UserClient userClient;


    @GetMapping("/call-account")
    public String callAccount() {
        return accountClient.callAccountService();
    }

    @GetMapping("/call-user")
    public String callUser() {
        return userClient.callUserService();

    }
}
