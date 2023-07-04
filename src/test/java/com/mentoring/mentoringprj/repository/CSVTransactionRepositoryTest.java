package com.mentoring.mentoringprj.repository;

import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.exceptions.AmountException;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;

import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;
import java.util.List;

import static com.mentoring.mentoringprj.domain.TransactionType.CREDIT;
import static com.mentoring.mentoringprj.domain.TransactionType.DEBIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CSVTransactionRepositoryTest {

    @Test
    void should_return_transactions() throws TransactionReadException, AmountException {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/goodTransactions.csv";
        CSVTransactionRepository repository = new CSVTransactionRepository(path);
        Transaction firstTransaction = Transaction.builder().name("transaction1").amount(300).date(LocalDateTime.parse("2023-03-15T13:14:15")).description("gold").type(DEBIT).build();
        Transaction secondTransaction = Transaction.builder().name("transaction2").amount(500).date(LocalDateTime.parse("2021-11-28T04:05:06")).description("silver").type(CREDIT).build();

        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).containsExactly(firstTransaction, secondTransaction);
    }

    @Test
    void should_not_return_an_empty_list_of_transactions_when_file_is_empty() throws TransactionReadException, AmountException {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/emptyFile.csv";
        CSVTransactionRepository repository = new CSVTransactionRepository(path);
        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).isEmpty();
    }

    @Test
    void should_throw_corrected_exception_when_amount_isNAN() {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/amountNan.csv";
        CSVTransactionRepository repository = new CSVTransactionRepository(path);
        TransactionReadException exception = assertThrows(TransactionReadException.class, repository::getTransactions);

        assertThat(exception).hasMessage("Incorrect amount");
    }

    @Test
    void should_throw_correct_exception_when_type_is_not_credit_or_debit() {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/unknownType.csv";
        CSVTransactionRepository repository = new CSVTransactionRepository(path);
        TransactionReadException exception = assertThrows(TransactionReadException.class, repository::getTransactions);

        assertThat(exception).hasMessage("could not parse transaction type");
    }
    @Test
    void should_throw_correct_exception_when_amount_is_zero_or_less() throws TransactionReadException, AmountException {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/emptyAmount.csv";
        CSVTransactionRepository repository = new CSVTransactionRepository(path);

        TransactionReadException exception = assertThrows(TransactionReadException.class, repository::getTransactions);

        assertThat(exception).hasMessage("Incorrect amount");
        assertThat(exception).hasCauseInstanceOf(AmountException.class);
    }
    @Test
    void should_throw_an_exception_when_add_transaction_from_CSV() throws Exception {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/goodTransactions.csv";
        CSVTransactionRepository repository = new CSVTransactionRepository(path);

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> repository.addTransaction(Transaction.builder().build()));


        assertThat(exception).hasMessage("Can't add a transaction using CSV repository");
    }
}