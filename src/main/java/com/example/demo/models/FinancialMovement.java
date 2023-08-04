package com.example.demo.models;

public class FinancialMovement {
    private String cardId;
    private Double amount;

    public FinancialMovement(String cardId, Double amount) {
        this.cardId = cardId;
        this.amount = amount;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
