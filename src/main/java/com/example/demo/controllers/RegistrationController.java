package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Verificar si el usuario ya existe en la base de datos por número de DNI o
        // correo electrónico
        User existingUserByDni = userRepository.findByDocumentNumber(user.getDocumentNumber());
        User existingUserByEmail = userRepository.findByEmail(user.getEmail());

        if (existingUserByDni != null) {
            return ResponseEntity.badRequest().body("Número de DNI ya registrado");
        }

        if (existingUserByEmail != null) {
            return ResponseEntity.badRequest().body("Correo electrónico ya registrado");
        }

        // Codificar la contraseña antes de guardarla en la base de datos
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Establecer la imagen predeterminada si el campo "image" está vacío
        if (user.getImage() == null || user.getImage().isEmpty()) {
            user.setImage("https://res.cloudinary.com/dylweuvjp/image/upload/v1691007184/vecqghtxym5pp8dwtmks.jpg");
        }

        // Guardar el usuario en la base de datos
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

}
