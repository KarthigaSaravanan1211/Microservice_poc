package com.wallet.wallet_user_service.repository;

import com.wallet.wallet_user_service.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        String sql = "INSERT INTO users(name, email) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getEmail());
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public List<User> findAll() {
        String sql = "SELECT user_id, name, email FROM users";
        return jdbcTemplate.query(sql,
                new BeanPropertyRowMapper<>(User.class));
    }

    public Optional<User> findById(Long userId) {
        String sql = "SELECT user_id, name, email FROM users WHERE user_id = ?";
        List<User> users = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(User.class),
                userId
        );
        return users.stream().findFirst();
    }

    public int update(User user) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE user_id = ?";
        return jdbcTemplate.update(
                sql,
                user.getName(),
                user.getEmail(),
                user.getUserId()
        );
    }

    public int deleteById(Long userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }
}
