package com.wallet.wallet_account_service.repository;

import com.wallet.wallet_account_service.model.Account;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createAccount(Long userId) {
        String sql = "INSERT INTO accounts(user_id, balance) VALUES (?, 0)";
        jdbcTemplate.update(sql, userId);
    }

    public void create(Long userId, BigDecimal balance) {
        String sql = "INSERT INTO accounts(user_id, balance) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, balance);
    }

    public Optional<Account> findByUserId(Long userId) {

        String sql = "SELECT account_id AS accountId, user_id AS userId, balance " +
                "FROM accounts WHERE user_id = ?";

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Account.class),
                userId
        ).stream().findFirst();
    }

    public void updateBalance(Long accountId, BigDecimal balance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, balance, accountId);
    }

    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM accounts WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
