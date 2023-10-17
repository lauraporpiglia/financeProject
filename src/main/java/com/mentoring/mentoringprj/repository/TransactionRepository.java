package com.mentoring.mentoringprj.repository;

import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.domain.TransactionWithoutId;
import com.mentoring.mentoringprj.exceptions.TransactionNotFoundException;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    List<Transaction> getTransactions() throws TransactionReadException;

    void addTransaction(Transaction transaction) throws TransactionReadException, IOException;
    void deleteTransaction(String id) throws TransactionReadException, IOException, TransactionNotFoundException;
    void updateTransaction(Transaction transaction) throws TransactionReadException, IOException, TransactionNotFoundException;
}
