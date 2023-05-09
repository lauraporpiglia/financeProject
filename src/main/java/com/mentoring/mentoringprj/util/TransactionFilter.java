package com.mentoring.mentoringprj.util;

import com.mentoring.mentoringprj.domain.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TransactionFilter {


    public List<Transaction> getTransactionsBetween(List<Transaction> transactions, LocalDateTime from, LocalDateTime to) {
        List<Transaction> filteredResults = new ArrayList<>();

        for (Transaction t : transactions) {
            LocalDateTime transactionDate = t.getDate();
            if ((transactionDate.isBefore(to) && transactionDate.isAfter(from))
                    || (transactionDate.equals(from)|| transactionDate.equals(to))){
                filteredResults.add(t);
            }
        }

        return filteredResults;

    }
}
