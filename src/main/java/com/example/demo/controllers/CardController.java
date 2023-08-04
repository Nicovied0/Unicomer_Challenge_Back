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

    private String generateUniqueCardNumber() {
        Random random = new Random();
        StringBuilder cardNumberBuilder = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            cardNumberBuilder.append(random.nextInt(10));
        }

        String generatedCardNumber = cardNumberBuilder.toString();
        while (cardRepository.existsByCardNumber(generatedCardNumber)) {
            cardNumberBuilder.setLength(0);
            for (int i = 0; i < 16; i++) {
                cardNumberBuilder.append(random.nextInt(10));
            }
            generatedCardNumber = cardNumberBuilder.toString();
        }

        return generatedCardNumber;
    }

    @PostMapping("/{userId}")
    public Card createCardForUser(@PathVariable String userId, @RequestBody Card card) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            card.setCardNumber(generateUniqueCardNumber());

            Card newCard = cardRepository.save(card);
            user.addCardId(newCard.getId());
            userRepository.save(user);
            return newCard;
        }
        return null;
    }

    @GetMapping("/{cardId}")
    public Card getCardDetails(@PathVariable String cardId) {
        return cardRepository.findById(cardId).orElse(null);
    }
}
