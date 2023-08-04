package com.example.demo.controllers;

import com.example.demo.models.Card;
import com.example.demo.models.Transaction;
import com.example.demo.repositories.CardRepository;
import com.example.demo.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

  private final TransactionRepository transactionRepository;
  private final CardRepository cardRepository;
  private final Set<Integer> usedTicketNumbers = new HashSet<>();

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

  @GetMapping("/{id}")
  public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
    Optional<Transaction> transaction = transactionRepository.findById(id);

    if (transaction.isPresent()) {
      return ResponseEntity.ok(transaction.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/withdraw")
  public Transaction simulateWithdrawal(@RequestBody Transaction transaction) {
    Card senderCard = cardRepository.findById(transaction.getSenderCardId()).orElse(null);

    if (senderCard == null) {
      throw new IllegalArgumentException("Tarjeta de envío no encontrada");
    }

    double amount = transaction.getAmount();
    double senderBalance = senderCard.getBalance();

    if (senderBalance < amount) {
      throw new IllegalArgumentException("Saldo insuficiente en la tarjeta de envío");
    }

    int ticketNumber = generateUniqueTicketNumber();

    senderCard.setBalance(senderBalance - amount);
    cardRepository.save(senderCard);

    transaction.setTransactionDate(new Date());
    transaction.setTicketNumber(ticketNumber);

    return transactionRepository.save(transaction);
  }

  @PostMapping("/deposit")
  @CrossOrigin(origins = "*")
  public Transaction simulateDeposit(@RequestBody Transaction transaction) {
      Card receiverCard = cardRepository.findById(transaction.getReceiverCardId()).orElse(null);

      if (receiverCard == null) {
          throw new IllegalArgumentException("Tarjeta de recepción no encontrada");
      }

      double amount = transaction.getAmount();
      double receiverBalance = receiverCard.getBalance();

      if (amount <= 0) {
          throw new IllegalArgumentException("El monto debe ser positivo");
      }
      int ticketNumber = generateUniqueTicketNumber();

      receiverCard.setBalance(receiverBalance + amount);
      cardRepository.save(receiverCard);
      transaction.setTransactionDate(new Date());
      transaction.setTicketNumber(ticketNumber);

      return transactionRepository.save(transaction);
  }


  private int generateUniqueTicketNumber() {
    Random random = new Random();
    int ticketNumber;

    do {
      ticketNumber = random.nextInt(100000);
    } while (usedTicketNumbers.contains(ticketNumber));

    usedTicketNumbers.add(ticketNumber);
    return ticketNumber;
  }

}
