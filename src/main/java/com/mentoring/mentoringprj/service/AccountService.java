package com.mentoring.mentoringprj.service;

import com.mentoring.mentoringprj.domain.AccountDetails;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.domain.TransactionWithoutId;
import com.mentoring.mentoringprj.exceptions.TransactionNotFoundException;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.repository.TransactionRepository;
import com.mentoring.mentoringprj.repository.entity.TransactionEntity;
import com.mentoring.mentoringprj.util.LocalDateTimeProvider;
import com.mentoring.mentoringprj.util.TransactionCalculator;
import com.mentoring.mentoringprj.util.TransactionFilter;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public AccountService(TransactionRepository repository,
                          TransactionCalculator calculator,
                          TransactionFilter filter,
                          LocalDateTimeProvider dateProvider) {
        this.repository = repository;
        this.calculator = calculator;
        this.filter = filter;
        this.dateProvider = dateProvider;
    }


    public AccountDetails getAccountDetails(Optional<LocalDateTime> from, Optional<LocalDateTime> to) throws TransactionReadException {
        List<Transaction> transactions = repository.findAll().stream()
                .map(TransactionEntity::toTransaction).toList();

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

    public AccountDetails saveTransaction(TransactionWithoutId transaction) throws TransactionReadException, IOException {
        Transaction transactionToAdd = transaction.toNewTransaction();
        TransactionEntity transactionEntity = transactionToAdd.toTransactionEntity();
        repository.save(transactionEntity);
        return getAccountDetails();
    }
    public AccountDetails saveTransaction(String id, TransactionWithoutId transaction) throws TransactionReadException {
        Transaction transactionToAdd = transaction.toNewTransaction();
        TransactionEntity transactionEntity = transactionToAdd.toTransactionEntity();
        transactionEntity.setId(id);

        repository.save(transactionEntity);

        return getAccountDetails();
    }


    public AccountDetails delete(String transactionId) throws TransactionReadException, TransactionNotFoundException, IOException {
      try{
          repository.deleteById(transactionId);
      }catch(EmptyResultDataAccessException erdae){
          throw new TransactionNotFoundException(String.format("transaction with id %s not found",transactionId));
      }
        return getAccountDetails();
    }

    public Optional<Transaction> getTransaction(String id) {
        return repository.findById(id)
                .map(TransactionEntity::toTransaction);
    }
}
