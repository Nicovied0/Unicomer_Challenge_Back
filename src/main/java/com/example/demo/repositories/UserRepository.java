package com.example.demo.repositories;

import com.example.demo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // Puedes agregar métodos adicionales relacionados con las operaciones de la
    // base de datos aquí

    User findByDocumentNumber(String documentNumber);

    User findByEmail(String email);

}