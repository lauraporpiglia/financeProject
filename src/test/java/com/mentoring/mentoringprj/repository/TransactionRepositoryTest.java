package com.mentoring.mentoringprj.repository;

import com.mentoring.mentoringprj.domain.Transaction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryTest {

    @Test
    void should_return_transactions() throws IOException {
        String path="/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/goodTransactions.csv";
        TransactionRepository repository = new TransactionRepository(path);
        Transaction firstTransaction = Transaction.builder().name("transaction1").amount(300).date(LocalDateTime.parse("2023-03-15T13:14:15")).description("gold").type("DEBIT").build();
        Transaction secondTransaction = Transaction.builder().name("transaction2").amount(500).date(LocalDateTime.parse("2021-11-28T04:05:06")).description("silver").type("CREDIT").build();

        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).containsExactly(firstTransaction, secondTransaction);
    }
}