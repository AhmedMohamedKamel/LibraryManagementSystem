package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public UserService(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    public Page<User> getAllUsers(Pageable pageable) {
        System.out.println("info: Fetching all users with pagination: " + pageable);
        Page<User> users = userRepository.findAll(pageable);
        System.out.println("debug: Number of users fetched: " + users.getNumberOfElements());
        return users;
    }

    public User getUserById(Long id) {
        System.out.println("info: Fetching user with id: " + id);
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
            System.out.println("debug: Found user: " + user.getName() + " with email: " + user.getEmail());
            return user;
        } catch (ResourceNotFoundException e) {
            System.out.println("error: " + e.getMessage());
            throw e;
        }
    }

    public User createUser(User user) {
        System.out.println("info: Creating new user with email: " + user.getEmail());
        userValidator.validateUniqueUser(user);
        userValidator.validatePasswordEncoding(user);
        User savedUser = userRepository.save(user);
        System.out.println("debug: User created with id: " + savedUser.getId());
        return savedUser;
    }

    public User updateUser(Long id, User updatedUser) {
        System.out.println("info: Updating user with id: " + id);
        User existingUser = getUserById(id);

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            System.out.println("debug: Updating password for user with id: " + id);
            userValidator.validatePasswordEncoding(updatedUser);
            existingUser.setPassword(updatedUser.getPassword());
        }

        User savedUser = userRepository.save(existingUser);
        System.out.println("debug: User updated with id: " + savedUser.getId());
        return savedUser;
    }

    public void deleteUser(Long id) {
        System.out.println("info: Deleting user with id: " + id);
        User user = getUserById(id);
        userRepository.delete(user);
        System.out.println("debug: User deleted with id: " + id);
    }
}
