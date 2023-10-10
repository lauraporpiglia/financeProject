package com.mentoring.mentoringprj.repository;

import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.domain.TransactionWithoutId;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;

import java.io.IOException;
import java.util.List;

public interface TransactionRepository {
    List<Transaction> getTransactions() throws TransactionReadException;
    List<Transaction> getTransactionsById(String transactionId) throws TransactionReadException;

    void addTransaction(Transaction transaction) throws TransactionReadException, IOException;
    void deleteTransaction(String id) throws TransactionReadException, IOException;
    void updateTransaction(Transaction transaction) throws TransactionReadException, IOException;
}
