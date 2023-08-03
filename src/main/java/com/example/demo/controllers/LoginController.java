package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.providers.JwtTokenProvider;
import com.example.demo.models.User;

import java.util.HashMap;
import java.util.Map;

@RestController
 @CrossOrigin(origins = "*")
public class LoginController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Autowired
    
    public LoginController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String documentType = request.get("documentType");
        String documentNumber = request.get("documentNumber");
        String password = request.get("password");

        User dbUser = userRepository.findByDocumentNumber(documentNumber);

        if (dbUser != null) {
            if (passwordEncoder.matches(password, dbUser.getPassword())) {
                String token = jwtTokenProvider.generateToken(documentNumber, documentType);

                // Crear el objeto JSON de respuesta
                Map<String, String> response = new HashMap<>();
                response.put("token", token);

                try {
                    // Convertir el objeto a una representación JSON
                    String jsonResponse = objectMapper.writeValueAsString(response);
                    return ResponseEntity.ok(jsonResponse);
                } catch (JsonProcessingException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error al procesar la respuesta");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}
