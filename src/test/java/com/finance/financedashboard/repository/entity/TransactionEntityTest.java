package com.finance.financedashboard.repository.entity;

import com.finance.financedashboard.domain.Transaction;
import com.finance.financedashboard.domain.TransactionType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TransactionEntityTest {
    @Test
    void shouldTranslateToTransaction() {
        TransactionEntity entity = TransactionEntity.builder()
                .amount(0)
                .id("ID")
                .date("2007-12-03T10:15:30")
                .description("desc")
                .name("name")
                .type(String.valueOf(TransactionType.CREDIT))
                .build();

        Transaction transaction = entity.toTransaction();

        assertThat(transaction.getId()).isEqualTo(entity.getId());
        assertThat(transaction.getAmount()).isEqualTo(entity.getAmount());
        assertThat(transaction.getDate()).isEqualTo(entity.getDate());
        assertThat(transaction.getDescription()).isEqualTo(entity.getDescription());
        assertThat(transaction.getName()).isEqualTo(entity.getName());
        assertThat(transaction.getType()).isEqualTo(TransactionType.valueOf(entity.getType()));
    }

}