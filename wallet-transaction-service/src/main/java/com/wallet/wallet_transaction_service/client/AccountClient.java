package com.wallet.wallet_transaction_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "wallet-account-service")
public interface AccountClient {

    @GetMapping("/accounts/test")
    String callAccountService();
}
