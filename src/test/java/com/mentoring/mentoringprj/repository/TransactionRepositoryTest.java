package com.mentoring.mentoringprj.repository;

import com.mentoring.mentoringprj.domain.Transaction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryTest {

    @Test
    void should_return_transactions() throws IOException {
        String path="/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/transactions.csv";
        TransactionRepository repository = new TransactionRepository(path);
        Transaction firstTransaction =  Transaction.builder().name("transaction1").amount(300).build();
        Transaction secondTransaction =  Transaction.builder().name("transaction2").amount(-500).build();

        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).containsExactly(firstTransaction,secondTransaction);

    }

}