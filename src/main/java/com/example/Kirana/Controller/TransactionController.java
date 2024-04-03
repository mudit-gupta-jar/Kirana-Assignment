package com.example.Kirana.Controller;

import com.example.Kirana.Model.Transaction;
import com.example.Kirana.Model.Week;
import com.example.Kirana.Repository.TransactionRepository;
import com.example.Kirana.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {
    @Autowired
     TransactionRepository transactionRepo;
    @Autowired
    TransactionService service;

    @GetMapping("/transactions")
    public ResponseEntity<?> getAllTransactions(){
     List<Transaction>transactionList=transactionRepo.findAll();
     try {
         Map<Transaction, Map<String, Double>> exchangeList = service.currencies(transactionList);

         return new ResponseEntity<>(transactionList, HttpStatus.OK);
     }
     catch (RestClientException e) {
         return new ResponseEntity<>("Failed to fetch exchange rates", HttpStatus.INTERNAL_SERVER_ERROR);
     }
    }
    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        try {
            if ("kirana_Admin".equalsIgnoreCase(transaction.getReceiver())) {
                transaction.setCredit(true);
            } else {
                transaction.setCredit(false);
            }
            service.getExchangeRate(transaction);
            Transaction newTransaction = transactionRepo.save(transaction);


            return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
      @GetMapping("/weekly-transactions")
    public ResponseEntity<?> weeklyTransactions(){

        List<Transaction>Transactions= service.findWeeklyTransactions();
//        Double netFlow= service.totalFlow(Transactions);
//        Week weekly= new Week(Transactions,netFlow);
        return new ResponseEntity<>(Transactions,HttpStatus.OK);
    }



}
