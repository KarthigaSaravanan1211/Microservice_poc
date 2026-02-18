package com.wallet.wallet_transaction_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "WALLET-USER-SERVICE")
public interface UserClient {

    @GetMapping("/users/test")
    String callUserService();
}
