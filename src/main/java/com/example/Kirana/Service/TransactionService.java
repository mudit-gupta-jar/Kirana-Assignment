package com.example.Kirana.Service;


import com.example.Kirana.Model.Week;
import com.example.Kirana.Model.Transaction;
import com.example.Kirana.Repository.TransactionRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transRepo;
    private final String API_URL = "https://api.fxratesapi.com/latest";

    public Map<String, Double>fetchCurrencyRates(){
        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(API_URL, String.class);
        JSONObject jsonObject = new JSONObject(response);
        JSONObject rates = jsonObject.getJSONObject("rates");
        // storing rates in HashMap
        Map<String, Double> exchangeRates = new TreeMap<>();
        for (String key : rates.keySet()) {
            exchangeRates.put(key, rates.getDouble(key));
        }
        return  exchangeRates;
    }
    public void getExchangeRate(Transaction transaction){
        Map<String, Double> exchangeRates= fetchCurrencyRates();
        Map<String, Double> convertedAmounts = new TreeMap<>();

        for (Map.Entry<String, Double> entry : exchangeRates.entrySet()) {
            double convertedAmount;

            if (transaction.getCurrency().equals("USD")) {
                convertedAmount = transaction.getAmount() * entry.getValue();
            } else if (Objects.equals(transaction.getCurrency(), entry.getKey())) {
                convertedAmount = transaction.getAmount();
            } else  {
                Double exRate=exchangeRates.get(transaction.getCurrency());
                convertedAmount = transaction.getAmount() / exRate;
                convertedAmount = convertedAmount * entry.getValue();
            }

            convertedAmounts.put(entry.getKey(), convertedAmount);
        }
        transaction.setOtherRates(convertedAmounts);

    }
    public Map<Transaction,Map<String,Double>>currencies(List<Transaction>transList){

        //API Fetching and making it in JSON
        Map<String, Double> exchangeRates= fetchCurrencyRates();

        // conversion logic for our transaction list
        Map<Transaction, Map<String, Double>> resultMap = new LinkedHashMap<>();
        for (Transaction transaction : transList) {
            Map<String, Double> convertedAmounts = new TreeMap<>();

            for (Map.Entry<String, Double> entry : exchangeRates.entrySet()) {
                double convertedAmount;

                if (transaction.getCurrency().equals("USD")) {
                    convertedAmount = transaction.getAmount() * entry.getValue();
                } else if (Objects.equals(transaction.getCurrency(), entry.getKey())) {
                    convertedAmount = transaction.getAmount();
                } else  {
                    Double exRate=exchangeRates.get(transaction.getCurrency());
                    convertedAmount = transaction.getAmount() / exRate;
                    convertedAmount = convertedAmount * entry.getValue();
                }

                convertedAmounts.put(entry.getKey(), convertedAmount);
            }
             transaction.setOtherRates(convertedAmounts);
            resultMap.put(transaction, convertedAmounts);
        }
     return resultMap;
    }
  public  List<Transaction> findWeeklyTransactions(){
       Instant oneWeekAgo = Instant.now().minusSeconds(7 * 24 * 60 * 60);
       List<Transaction> transactions = transRepo.findByCreatedAtGreaterThanEqual(oneWeekAgo.toString());

       return transactions;
   }

   public Double getUsdAmount(Transaction transaction){
       Map<String, Double> exchangeRates= fetchCurrencyRates();

       return transaction.getAmount()/ exchangeRates.get(transaction.getCurrency());
   }
   public Double totalFlow(List<Transaction> transactions){
        double netFlow= 0.0;
        for(Transaction transaction: transactions){
            if(transaction.getCredit()==Boolean.TRUE){
                netFlow= netFlow + getUsdAmount(transaction);
            }
            else{
                netFlow= netFlow - getUsdAmount(transaction);
            }

        }
        return netFlow;
   }
}
