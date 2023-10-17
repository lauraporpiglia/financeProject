package com.mentoring.mentoringprj.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


class TransactionWithoutIdTest {
    private TransactionWithoutId transactionWithoutId;

    @BeforeEach
    void setup(){
        transactionWithoutId = TransactionWithoutId.builder()
                .type(TransactionType.DEBIT)
                .amount(500)
                .name("TRANSACTION NAME")
                .description("TRANSACTION DESCRIPTION")
                .date(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldConvertTransactionCorrectly() {
        Transaction transaction = transactionWithoutId.toNewTransaction();
        
        assertThat(transaction.getId()).isNotNull();
        assertThat(transaction.getType()).isEqualTo(transactionWithoutId.getType());
        assertThat(transaction.getAmount()).isEqualTo(transactionWithoutId.getAmount());
        assertThat(transaction.getName()).isEqualTo(transactionWithoutId.getName());
        assertThat(transaction.getDescription()).isEqualTo(transactionWithoutId.getDescription());
        assertThat(transaction.getDate()).isEqualTo(transactionWithoutId.getDate());
    }
    @Test
    void shouldReturnNewTransactionEachTime() {
        Transaction transaction1 = transactionWithoutId.toNewTransaction();
        Transaction transaction2 = transactionWithoutId.toNewTransaction();

        assertThat(transaction1.getId()).isNotEqualTo(transaction2.getId());
    }

}