package com.mentoring.mentoringprj.util;

import com.mentoring.mentoringprj.domain.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;



import static org.assertj.core.api.Assertions.assertThat;


class TransactionFilterTest {
    @Test
    void should_return_empty_list(){
        //GIVEN
        TransactionFilter filter = new TransactionFilter();
        List<Transaction> allTransactions = Collections.emptyList();

        //WHEN
        List<Transaction> results = filter.getTransactionsBetween(allTransactions, LocalDateTime.now().minusDays(1), LocalDateTime.now());
        //THEN
        assertThat(results).isEmpty();
    }
    //fix code to be inclusive
    @Test
    void should_return_transactions_between_dates_including_edges(){
        TransactionFilter filter = new TransactionFilter();
        LocalDateTime fromDate = LocalDateTime.parse("2023-03-15T13:14:00");
        LocalDateTime toDate = LocalDateTime.parse("2023-03-15T13:14:59");

        Transaction earlyOutOfTimeWindowTransaction = Transaction.builder().name("early").date(fromDate.minusSeconds(1)).build();
        Transaction earlyInTimeWindowTransaction = Transaction.builder().name("earlyintime").date(fromDate).build();
        Transaction lateInTimeWindowTransaction = Transaction.builder().name("laterintime").date(toDate).build();
        Transaction laterOutOfTimeWindowTransaction = Transaction.builder().name("later").date(toDate.plusSeconds(1)).build();

        List<Transaction> allTransactions = List.of(earlyOutOfTimeWindowTransaction, earlyInTimeWindowTransaction, lateInTimeWindowTransaction,laterOutOfTimeWindowTransaction);

        List<Transaction> results = filter.getTransactionsBetween(allTransactions, fromDate, toDate);

        assertThat(results).containsExactly(earlyInTimeWindowTransaction,lateInTimeWindowTransaction);
    }

}