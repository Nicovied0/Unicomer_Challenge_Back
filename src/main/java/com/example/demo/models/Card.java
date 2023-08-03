package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "cards")
public class Card {

    @Id
    private String id;
    private Date expirationDate;
    private double balance;
    private String cardHolderName;
    private String cardNumber;

    // Constructor, getters y setters
    public Card() {
    }

    public Card(Date expirationDate, double balance, String cardHolderName) {
        this.expirationDate = expirationDate;
        this.balance = balance;
        this.cardHolderName = cardHolderName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", expirationDate=" + expirationDate +
                ", balance=" + balance +
                ", cardHolderName='" + cardHolderName + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                '}';
    }
}
