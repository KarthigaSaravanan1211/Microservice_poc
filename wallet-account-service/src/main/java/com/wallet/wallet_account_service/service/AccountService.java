package com.wallet.wallet_account_service.service;

import com.wallet.wallet_account_service.exception.AccountNotFoundException;
import com.wallet.wallet_account_service.exception.InsufficientBalanceException;
import com.wallet.wallet_account_service.model.Account;
import com.wallet.wallet_account_service.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    public void createAccount(Long userId) {
        repo.createAccount(userId);
    }

    public void createAccount(Account account) {
        repo.create(account.getUserId(), account.getBalance());
    }

    public Account getAccountByUserId(Long userId) {
        return repo.findByUserId(userId)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found for userId: " + userId));
    }

    public BigDecimal credit(Long userId, BigDecimal amount) {

        Account account = getAccountByUserId(userId);

        BigDecimal newBalance =
                account.getBalance().add(amount);

        repo.updateBalance(account.getAccountId(), newBalance);

        return newBalance;
    }

    public BigDecimal debit(Long userId, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        Account account = getAccountByUserId(userId);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        BigDecimal newBalance =
                account.getBalance().subtract(amount);

        repo.updateBalance(account.getAccountId(), newBalance);

        return newBalance;
    }

    public void deleteAccount(Long userId) {
        repo.deleteByUserId(userId);
    }
}
