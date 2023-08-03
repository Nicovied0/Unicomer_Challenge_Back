package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Método para crear un nuevo usuario (POST)
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        // Codificar la contraseña antes de guardarla en la base de datos
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Guardar el usuario en la base de datos
        return userRepository.save(user);
    }


    // Método para obtener una lista de todos los usuarios (GET)
    @GetMapping("/users")
    public List<User> getAllUsers() {
        // Utiliza el UserRepository para obtener todos los usuarios de la base de datos
        return userRepository.findAll();
    }

    @GetMapping("/users/{documentNumber}")
    public ResponseEntity<User> getUserByDocumentNumber(@PathVariable String documentNumber) {
        User user = userRepository.findByDocumentNumber(documentNumber);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
