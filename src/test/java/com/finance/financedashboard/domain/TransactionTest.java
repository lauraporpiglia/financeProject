package com.finance.financedashboard.domain;

import com.finance.financedashboard.repository.entity.TransactionEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TransactionTest {
    @Test
    void shouldConvertToTransactionEntity(){
        Transaction transaction = Transaction.builder()
                .amount(0)
                .id("ID")
                .date(LocalDateTime.parse("2007-12-03T10:15:30"))
                .description("desc")
                .name("name")
                .type(TransactionType.CREDIT)
                .build();

        TransactionEntity transactionEntity = transaction.toTransactionEntity();

        assertThat(transactionEntity.getId()).isEqualTo(transaction.getId());
        assertThat(transactionEntity.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(transactionEntity.getDate()).isEqualTo(transaction.getDate().toString());
        assertThat(transactionEntity.getDescription()).isEqualTo(transaction.getDescription());
        assertThat(transactionEntity.getName()).isEqualTo(transaction.getName());
        assertThat(transactionEntity.getType()).isEqualTo(transaction.getType().toString());
    }

    @Test
    void shouldConvertToTransactionEntityWithNullFields(){
        Transaction transaction = Transaction.builder()
                .amount(0)
                .id("ID")
                .description("desc")
                .name("name")
                .date(null)
                .type(null)
                .build();

        TransactionEntity transactionEntity = transaction.toTransactionEntity();

        assertThat(transactionEntity.getId()).isEqualTo(transaction.getId());
        assertThat(transactionEntity.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(transactionEntity.getDescription()).isEqualTo(transaction.getDescription());
        assertThat(transactionEntity.getName()).isEqualTo(transaction.getName());
        assertThat(transactionEntity.getDate()).isNull();
        assertThat(transactionEntity.getType()).isNull();
    }
}