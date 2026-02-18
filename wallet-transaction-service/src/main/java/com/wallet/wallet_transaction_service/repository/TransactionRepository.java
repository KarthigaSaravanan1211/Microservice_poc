package com.wallet.wallet_transaction_service.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Long save(Long fromUserId, Long toUserId, BigDecimal amount, String status) {
        String sql = """
        INSERT INTO transactions(from_user_id, to_user_id, amount, status)
        VALUES (?, ?, ?, ?)
        RETURNING transaction_id
        """;

        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                fromUserId,
                toUserId,
                amount,
                status
        );
    }


    public void updateStatus(Long transactionId, String status) {
        String sql = "UPDATE transactions SET status = ? WHERE transaction_id = ?";
        jdbcTemplate.update(sql, status, transactionId);
    }

    public String getStatus(Long transactionId) {
        String sql = "SELECT status FROM transactions WHERE transaction_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, transactionId);
    }

    public List<Map<String, Object>> findByUserId(Long userId) {
        String sql = """
            SELECT * FROM transactions
            WHERE from_user_id = ? OR to_user_id = ?
            ORDER BY created_at DESC
            """;

        return jdbcTemplate.queryForList(sql, userId, userId);
    }


}
