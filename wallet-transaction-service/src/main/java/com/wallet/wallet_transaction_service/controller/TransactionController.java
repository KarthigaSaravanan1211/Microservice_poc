package com.wallet.wallet_transaction_service.controller;

import com.wallet.wallet_transaction_service.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/transfer")
    public Map<String, Object> transfer(
            @RequestParam Long fromUserId,
            @RequestParam Long toUserId,
            @RequestParam BigDecimal amount) {

        Long transactionId = service.transfer(fromUserId, toUserId, amount);
        String status = service.getTransactionStatus(transactionId);

        return Map.of(
                "transaction_id", transactionId,
                "from_user_id", fromUserId,
                "to_user_id", toUserId,
                "amount", amount,
                "status", status
        );
    }


    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getUserTransactions(@PathVariable Long userId) {
        return service.getTransactions(userId);
    }

}
