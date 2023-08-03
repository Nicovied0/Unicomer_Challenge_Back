package com.example.demo.repositories;

import com.example.demo.models.Card;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CardRepository extends MongoRepository<Card, String> {
    boolean existsByCardNumber(String cardNumber);
    
}

