package com.finance.financedashboard.util;

import com.finance.financedashboard.domain.Transaction;
import com.finance.financedashboard.domain.TransactionType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionCalculatorTest {

    @Test
    void given_two_transactions_calculate_sum(){
        TransactionCalculator calculator = new TransactionCalculator() ;
        List<Transaction> transactionList = List.of(
                Transaction.builder().amount(1111).type(TransactionType.CREDIT).build(),
                Transaction.builder().amount(2222).type(TransactionType.CREDIT).build()
        );

        long total = calculator.calculateTotal(transactionList);
        assertThat(total).isEqualTo(3333);
    }

    @Test
    void given_two_transactions_calculate_sum_withDebit(){
        TransactionCalculator calculator = new TransactionCalculator() ;
        List<Transaction> transactionList = List.of(
                Transaction.builder().amount(100).type(TransactionType.CREDIT).build(),
                Transaction.builder().amount(70).type(TransactionType.DEBIT).build()
        );

        long total = calculator.calculateTotal(transactionList);
        assertThat(total).isEqualTo(30);
    }

    @Test
    void given_two_transactions_calculate_sum_withNegativeTotal(){
        TransactionCalculator calculator = new TransactionCalculator() ;
        List<Transaction> transactionList = List.of(
                Transaction.builder().amount(100).type(TransactionType.CREDIT).build(),
                Transaction.builder().amount(110).type(TransactionType.DEBIT).build()
        );

        long total = calculator.calculateTotal(transactionList);
        assertThat(total).isEqualTo(-10);
    }

}