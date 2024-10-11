package com.example.usermanagementsystem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // No additional methods needed for basic CRUD operations
}