package com.example.Kirana.Model;

import com.example.Kirana.Model.Transaction;
import com.example.Kirana.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Week {
    public List<Transaction> weeklyTransactions;
    public Double netFlow;

    public Week(List<Transaction> weeklyTransactions,Double netFlow){
        this.weeklyTransactions=weeklyTransactions;
        this.netFlow=netFlow;
    }
}
