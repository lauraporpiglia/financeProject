package com.mentoring.mentoringprj.util;

import com.mentoring.mentoringprj.domain.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TransactionFilter {


    public List<Transaction> getTransactionsBetween(List<Transaction> transactions, LocalDateTime from, LocalDateTime to) {
        List<Transaction> filteredResults = new ArrayList<>();

        for (Transaction t : transactions) {
            LocalDateTime transactionDate = t.getDate();
            if ((transactionDate.isBefore(to) && transactionDate.isAfter(from))
                    || (transactionDate.equals(from) || transactionDate.equals(to))) {
                filteredResults.add(t);
            }
        }

        return filteredResults;

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
