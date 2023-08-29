package com.mentoring.mentoringprj.util;

import com.mentoring.mentoringprj.domain.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


import static org.assertj.core.api.Assertions.assertThat;


class TransactionFilterTest {

    private static final String SEARCH_STRING = "matching";

    @Test
    void should_return_empty_list() {
        //GIVEN
        TransactionFilter filter = new TransactionFilter();
        List<Transaction> allTransactions = Collections.emptyList();

        //WHEN
        List<Transaction> results = filter.getTransactionsBetween(allTransactions, LocalDateTime.now().minusDays(1), LocalDateTime.now());
        //THEN
        assertThat(results).isEmpty();
    }

    @Test
    void should_return_transactions_between_dates_including_edges() {
        TransactionFilter filter = new TransactionFilter();
        LocalDateTime fromDate = LocalDateTime.parse("2023-03-15T13:14:00");
        LocalDateTime toDate = LocalDateTime.parse("2023-03-15T13:14:59");

        Transaction earlyOutOfTimeWindowTransaction = Transaction.builder().name("early").date(fromDate.minusSeconds(1)).build();
        Transaction earlyInTimeWindowTransaction = Transaction.builder().name("earlyintime").date(fromDate).build();
        Transaction middleTransaction = Transaction.builder().name("middleTransaction").date(fromDate.plusSeconds(30)).build();
        Transaction lateInTimeWindowTransaction = Transaction.builder().name("laterintime").date(toDate).build();
        Transaction laterOutOfTimeWindowTransaction = Transaction.builder().name("later").date(toDate.plusSeconds(1)).build();

        List<Transaction> allTransactions = List.of(earlyOutOfTimeWindowTransaction,
                earlyInTimeWindowTransaction,
                middleTransaction,
                lateInTimeWindowTransaction,
                laterOutOfTimeWindowTransaction);

        List<Transaction> results = filter.getTransactionsBetween(allTransactions, fromDate, toDate);

        assertThat(results).containsExactly(earlyInTimeWindowTransaction, middleTransaction, lateInTimeWindowTransaction);
    }

    @Test
    void should_return_multiple_transaction_when_both_name_and_description_match() {
        TransactionFilter filter = new TransactionFilter();
        Transaction matchingTransaction1 = Transaction.builder().name("matching name").description("description").build();
        Transaction matchingTransaction2 = Transaction.builder().name("name").description("matching description").build();
        Transaction otherTransaction = Transaction.builder().name("name").description("wrong description").build();
        List<Transaction> results = filter.getFilteredTransactionByNameOrDescription(List.of(matchingTransaction1, matchingTransaction2, otherTransaction), SEARCH_STRING);

        assertThat(results).containsExactly(matchingTransaction1, matchingTransaction2);
    }
    @ParameterizedTest
    @MethodSource("provideNameFilterScenarios")
    void test_get_filtered_transactions_by_name(String name, String description, String search, boolean isTransactionMatching) {
        TransactionFilter filter = new TransactionFilter();
        Transaction transactionToMatch = Transaction.builder().name(name).description(description).build();
        Transaction otherTransaction = Transaction.builder().name("wrong name").description("wrong description").build();
        List<Transaction> results = filter.getFilteredTransactionByNameOrDescription(List.of(transactionToMatch, otherTransaction), search);

        if(isTransactionMatching){
            assertThat(results).containsExactly(transactionToMatch);
        } else{
            assertThat(results).isEmpty();
        }

    }

    private static Stream<Arguments> provideNameFilterScenarios() {

        return Stream.of(
                Arguments.of("matching name","description","matching", true),
                Arguments.of("matching name","description","Matching", true),
                Arguments.of("matching name","description","MATCHING", true),
                Arguments.of("MATCHING name","description","matching", true),
                Arguments.of("MATCHING name","description","pie", false),

                Arguments.of("name","matching description","matching", true),
                Arguments.of("name","matching description","Matching", true),
                Arguments.of("name","matching description","MATCHING", true),
                Arguments.of("name","MATCHING description","matching", true),
                Arguments.of("name","MATCHING description","pie", false),

                Arguments.of("matching name","matching description","matching", true)

        );

    }
}

