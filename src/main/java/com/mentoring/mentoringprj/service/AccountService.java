package com.mentoring.mentoringprj.service;

import com.mentoring.mentoringprj.domain.AccountDetails;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.repository.TransactionRepository;
import com.mentoring.mentoringprj.util.TransactionCalculator;
import com.mentoring.mentoringprj.util.TransactionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final TransactionRepository repository;
    private final TransactionCalculator calculator;

    private final TransactionFilter filter;

    public AccountDetails getAccountDetails() throws TransactionReadException {
        List<Transaction> transactions = repository.getTransactions();
        long balance = this.calculator.calculateTotal(transactions);
        return AccountDetails.builder()
                .balance(balance)
                .transactions(transactions)
                .build();
    }

    public AccountDetails getAccountDetails(LocalDateTime from, LocalDateTime to) throws TransactionReadException {
        List<Transaction> transactions = repository.getTransactions();
        List<Transaction> filteredTransactions = filter.getTransactionsBetween(transactions, from, to);
        long balance = this.calculator.calculateTotal(filteredTransactions);
        return AccountDetails.builder()
                .balance(balance)
                .transactions(filteredTransactions)
                .build();
    }
}
