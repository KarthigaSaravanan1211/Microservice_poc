package com.wallet.wallet_account_service.controller;

import com.wallet.wallet_account_service.model.Account;
import com.wallet.wallet_account_service.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createAccount(@PathVariable Long userId) {

        service.createAccount(userId);

        return ResponseEntity.ok(
                Map.of("message", "Account created", "userId", userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Account> getAccount(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getAccountByUserId(userId));
    }

    @PostMapping("/credit/{userId}")
    public ResponseEntity<?> credit(
            @PathVariable Long userId,
            @RequestParam BigDecimal amount) {

        BigDecimal balance = service.credit(userId, amount);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Amount credited successfully",
                        "userId", userId,
                        "newBalance", balance
                ));
    }

    @PostMapping("/debit/{userId}")
    public ResponseEntity<?> debit(
            @PathVariable Long userId,
            @RequestParam BigDecimal amount) {

        BigDecimal balance = service.debit(userId, amount);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Amount debited successfully",
                        "userId", userId,
                        "newBalance", balance
                ));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long userId) {

        service.deleteAccount(userId);

        return ResponseEntity.ok(
                Map.of("message", "Account deleted", "userId", userId));
    }
}
