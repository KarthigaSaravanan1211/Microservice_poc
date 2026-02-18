package com.wallet.wallet_user_service.service;

import com.wallet.wallet_user_service.exception.DuplicateUserException;
import com.wallet.wallet_user_service.exception.UserNotFoundException;
import com.wallet.wallet_user_service.model.User;
import com.wallet.wallet_user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User createUser(User user) {

        if (repo.existsByEmail(user.getEmail())) {
            throw new DuplicateUserException("Email already exists");
        }
        repo.save(user);
        return user;
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User getUserById(Long userId) {
        return repo.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id: " + userId));
    }

    public User updateUser(Long userId, User user) {
        User existingUser = getUserById(userId);
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        repo.update(existingUser);
        return existingUser;
    }

    public void deleteUser(Long userId) {

        getUserById(userId);
        repo.deleteById(userId);
    }
}
