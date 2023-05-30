package com.mentoring.mentoringprj.repository;

import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;

import java.util.List;

public interface TransactionRepository {
    List<Transaction> getTransactions() throws TransactionReadException;
}
