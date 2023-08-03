package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;
    private String senderCardId;
    private String receiverCardId;
    private double amount;
    private Date transactionDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderCardId() {
        return senderCardId;
    }

    public void setSenderCardId(String senderCardId) {
        this.senderCardId = senderCardId;
    }

    public String getReceiverCardId() {
        return receiverCardId;
    }

    public void setReceiverCardId(String receiverCardId) {
        this.receiverCardId = receiverCardId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
