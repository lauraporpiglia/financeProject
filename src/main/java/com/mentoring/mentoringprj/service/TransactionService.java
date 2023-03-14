package com.mentoring.mentoringprj.service;

import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;

    public List<Transaction> getTransactions() {
        return repository.getTransactions();
    }
}
