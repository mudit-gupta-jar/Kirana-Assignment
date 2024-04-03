package com.example.Kirana.Model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "transactions")
public class Transaction {
    @Id
    public String id;
    public String sender;
    public String receiver;
    public Boolean credit;
    public Double amount;
    public String currency;
    public Map<String,Double>otherRates;

    public Map<String, Double> getOtherRates() {
        return otherRates;
    }

    public void setOtherRates(Map<String, Double> otherRates) {
        this.otherRates = otherRates;
    }
    public Double getUsdRate(){
        return otherRates.get("USD");
    }



    public String createdAt;

    // Getters and Setters
    // ...

    public Transaction() {
        this.createdAt = Instant.now().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Boolean getCredit() {
        return credit;
    }

    public void setCredit(Boolean credit) {
        this.credit = credit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
