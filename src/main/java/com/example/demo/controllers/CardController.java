package com.example.demo.controllers;

import com.example.demo.models.Card;
import com.example.demo.models.User;
import com.example.demo.repositories.CardRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "*")
public class CardController {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Autowired
    public CardController(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    // Método para generar un número de tarjeta único
    private String generateUniqueCardNumber() {
        Random random = new Random();
        StringBuilder cardNumberBuilder = new StringBuilder();

        // Generar 16 dígitos aleatorios
        for (int i = 0; i < 16; i++) {
            cardNumberBuilder.append(random.nextInt(10));
        }

        String generatedCardNumber = cardNumberBuilder.toString();

        // Verificar si el número de tarjeta ya existe en la base de datos
        // Si existe, generar uno nuevo hasta obtener un número único
        while (cardRepository.existsByCardNumber(generatedCardNumber)) {
            cardNumberBuilder.setLength(0); // Limpiar el StringBuilder
            for (int i = 0; i < 16; i++) {
                cardNumberBuilder.append(random.nextInt(10));
            }
            generatedCardNumber = cardNumberBuilder.toString();
        }

        return generatedCardNumber;
    }

    // Endpoint para crear una nueva tarjeta asociada a un usuario
    @PostMapping("/{userId}")
    public Card createCardForUser(@PathVariable String userId, @RequestBody Card card) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            // Asignar un número de tarjeta único antes de guardar la tarjeta
            card.setCardNumber(generateUniqueCardNumber());

            Card newCard = cardRepository.save(card);
            user.addCardId(newCard.getId());
            userRepository.save(user);
            return newCard;
        }
        return null;
    }

    // Endpoint para obtener información detallada de una tarjeta por su ID
    @GetMapping("/{cardId}")
    public Card getCardDetails(@PathVariable String cardId) {
        return cardRepository.findById(cardId).orElse(null);
    }
}
