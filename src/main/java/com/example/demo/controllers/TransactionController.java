package com.example.demo.controllers;

import com.example.demo.models.Card;
import com.example.demo.models.Transaction;
import com.example.demo.repositories.CardRepository;
import com.example.demo.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

  private final TransactionRepository transactionRepository;
  private final CardRepository cardRepository;

  @Autowired
  public TransactionController(TransactionRepository transactionRepository, CardRepository cardRepository) {
    this.transactionRepository = transactionRepository;
    this.cardRepository = cardRepository;
  }

  @PostMapping("/create")
  public Transaction createTransaction(@RequestBody Transaction transaction) {
    Card senderCard = cardRepository.findById(transaction.getSenderCardId()).orElse(null);
    Card receiverCard = cardRepository.findById(transaction.getReceiverCardId()).orElse(null);

    if (senderCard == null || receiverCard == null) {
      throw new IllegalArgumentException("Tarjeta de envío o recepción no encontrada");
    }

    if (senderCard.getBalance() < transaction.getAmount()) {
      throw new IllegalArgumentException("Saldo insuficiente en la tarjeta de envío");
    }

    senderCard.setBalance(senderCard.getBalance() - transaction.getAmount());
    receiverCard.setBalance(receiverCard.getBalance() + transaction.getAmount());

    cardRepository.save(senderCard);
    cardRepository.save(receiverCard);

    transaction.setTransactionDate(new Date());

    return transactionRepository.save(transaction);
  }

  @GetMapping("/all")
  public List<Transaction> getAllTransactions() {
    return transactionRepository.findAll();
  }
}
