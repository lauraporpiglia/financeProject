package com.mentoring.mentoringprj.service;

import com.mentoring.mentoringprj.domain.AccountDetails;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.domain.TransactionWithoutId;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.repository.TransactionRepository;
import com.mentoring.mentoringprj.util.LocalDateTimeProvider;
import com.mentoring.mentoringprj.util.TransactionCalculator;
import com.mentoring.mentoringprj.util.TransactionFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final TransactionRepository repository;
    private final TransactionCalculator calculator;

    private final TransactionFilter filter;

    private final LocalDateTimeProvider dateProvider;

    public AccountService(@Qualifier("json") TransactionRepository repository,
                          TransactionCalculator calculator,
                          TransactionFilter filter,
                          LocalDateTimeProvider dateProvider) {
        this.repository = repository;
        this.calculator = calculator;
        this.filter = filter;
        this.dateProvider = dateProvider;
    }


    public AccountDetails getAccountDetails(Optional<LocalDateTime> from, Optional<LocalDateTime> to) throws TransactionReadException {
        List<Transaction> transactions = repository.getTransactions();

        if (from.isPresent() && to.isPresent()) {
            transactions = filter.getTransactionsBetween(transactions, from.get(), to.get());
        } else if (from.isPresent()) {
            transactions = filter.getTransactionsBetween(transactions, from.get(), dateProvider.now());
        } else if (to.isPresent()) {
            transactions = filter.getTransactionsBetween(transactions, LocalDateTime.MIN, to.get());
        }

        long balance = this.calculator.calculateTotal(transactions);
        return AccountDetails.builder()
                .balance(balance)
                .transactions(transactions)
                .build();
    }

    public AccountDetails getAccountDetails() throws TransactionReadException {
        return getAccountDetails(Optional.empty(), Optional.empty());
    }

    public AccountDetails addTransaction(Transaction transaction) throws TransactionReadException, IOException {
        repository.addTransaction(transaction);
        return getAccountDetails();
    }


    public AccountDetails delete(String transactionId) throws TransactionReadException, IOException {
        repository.deleteTransaction(transactionId);
        return getAccountDetails();
    }

    public AccountDetails updateTransaction(String id, TransactionWithoutId transactionToUpdate) throws TransactionReadException, IOException {
        Transaction transaction = Transaction.builder()
                .id(id)
                .name(transactionToUpdate.getName())
                .description(transactionToUpdate.getDescription())
                .type(transactionToUpdate.getType())
                .amount(transactionToUpdate.getAmount())
                .date(transactionToUpdate.getDate())
                .build();
      repository.updateTransaction(transaction);
        return getAccountDetails();
    }
}
