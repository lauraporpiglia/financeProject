package com.mentoring.mentoringprj.service;

import com.mentoring.mentoringprj.domain.AccountDetails;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.repository.TransactionRepository;
import com.mentoring.mentoringprj.util.TransactionCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final TransactionRepository repository;
    private final TransactionCalculator calculator;

    public AccountDetails getAccountDetails() throws TransactionReadException {
        List<Transaction> transactions = repository.getTransactions();
        long balance = this.calculator.calculateTotal(transactions);
        return AccountDetails.builder()
                .balance(balance)
                .transactions(transactions)
                .build();
    }
}
