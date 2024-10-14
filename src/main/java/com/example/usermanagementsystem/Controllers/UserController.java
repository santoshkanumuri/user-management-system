package com.example.usermanagementsystem.Controllers;

import com.example.usermanagementsystem.Entities.User;
import com.example.usermanagementsystem.Exceptions.EmailAlreadyExistsException;
import com.example.usermanagementsystem.Exceptions.UserAlreadyExistsException;
import com.example.usermanagementsystem.Repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HttpCodeStatusMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpCodeStatusMapper healthHttpCodeStatusMapper;

    // Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        else {
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
    }

    // Get all users
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<User> findByName(@PathVariable String name) {
        Optional<User> user=userRepository.findByName(name);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            userRepository.save(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // delete all users if http body contains a password field and it is password
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllUsers(@RequestBody User user) {
        if (user.getName().equals("santosh") && user.getEmail().equals("santosh@gmail.com")) {
            userRepository.deleteAll();
            return ResponseEntity.noContent().build();
        } else {
            //return message "invalid user" with status code 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
