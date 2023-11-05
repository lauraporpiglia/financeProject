package com.mentoring.mentoringprj.util;

import com.mentoring.mentoringprj.domain.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


@Component
public class TransactionFilter {
    public List<Transaction> getTransactionsBetween(List<Transaction> transactions, LocalDateTime from, LocalDateTime to) {
        return transactions.stream()
                .filter(t -> {
                    LocalDateTime transactionDate = t.getDate();
                    return (transactionDate.isBefore(to) && transactionDate.isAfter(from))
                            || transactionDate.equals(from)
                            || transactionDate.equals(to);
                })
                .toList();
    }

    public List<Transaction> getFilteredTransactionByNameOrDescription(List<Transaction> transactions, String search) {
        return transactions.stream()
                .filter(transaction -> {
                    boolean nameMatches = transaction.getName().toLowerCase().contains(search.toLowerCase());
                    boolean descriptionMatches = transaction.getDescription().toLowerCase().contains(search.toLowerCase());

                    return nameMatches || descriptionMatches;
                })
                .toList();
    }
}
