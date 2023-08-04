package com.example.demo.controllers;

import com.example.demo.models.FinancialMovement;
import com.example.demo.models.IncomeExpenseRequest;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/dni/{documentNumber}")
    public ResponseEntity<User> getUserByDocumentNumber(@PathVariable String documentNumber) {
        User user = userRepository.findByDocumentNumber(documentNumber);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/id/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/ingresos")
    public List<Double> getIngresos(@PathVariable String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getIngresos();
        }
        return null;
    }

    @GetMapping("/{userId}/egresos")
    public List<Double> getEgresos(@PathVariable String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getEgresos();
        }
        return null;
    }

    @PostMapping("/users/{userId}/ingresos/{cardId}")
    public ResponseEntity<User> addIngreso(@PathVariable String userId, @PathVariable String cardId,
            @RequestBody IncomeExpenseRequest request) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            if (user.getCardIds().contains(cardId)) {
                user.addIngreso(request.getAmount());

                FinancialMovement financialMovement = new FinancialMovement(cardId, request.getAmount());
                user.addFinancialMovement(financialMovement);

                userRepository.save(user);
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
            }
        }
        return ResponseEntity.notFound().build(); 
    }

    @PostMapping("/users/{userId}/egresos/{cardId}")
    public ResponseEntity<User> addEgreso(@PathVariable String userId, @PathVariable String cardId,
            @RequestBody IncomeExpenseRequest request) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            if (user.getCardIds().contains(cardId)) {
                user.addEgreso(request.getAmount());

                FinancialMovement financialMovement = new FinancialMovement(cardId, -request.getAmount());
                user.addFinancialMovement(financialMovement);

                userRepository.save(user);
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
            }
        }
        return ResponseEntity.notFound().build();
    }
}