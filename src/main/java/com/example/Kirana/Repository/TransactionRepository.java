package com.example.Kirana.Repository;

import com.example.Kirana.Model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction,String> {
    List<Transaction> findByCreatedAtGreaterThanEqual(String date);
}
