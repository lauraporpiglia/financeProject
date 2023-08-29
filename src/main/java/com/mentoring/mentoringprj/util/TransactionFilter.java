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
        Set<Transaction> filteredResults = new HashSet<>();


        List<Transaction> nameResults = transactions.stream()
                .filter(transaction -> transaction.getName().toLowerCase().contains(search.toLowerCase()))
                .toList();


        List<Transaction> descResults = transactions.stream()
                .filter(transaction -> transaction.getDescription().toLowerCase().contains(search.toLowerCase()))
                .toList();

        filteredResults.addAll(nameResults);
        filteredResults.addAll(descResults);


        return filteredResults.stream().toList();

    }


}
