package com.example.usermanagementsystem.Repositories;

import com.example.usermanagementsystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // No additional methods needed for basic CRUD operations
    Optional<User> findByName(String email);

    Optional<User> findByEmail(String email);
    }
