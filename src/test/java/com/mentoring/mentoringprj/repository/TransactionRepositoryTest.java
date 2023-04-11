package com.mentoring.mentoringprj.repository;

import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;

import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;
import java.util.List;

import static com.mentoring.mentoringprj.domain.TransactionType.CREDIT;
import static com.mentoring.mentoringprj.domain.TransactionType.DEBIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionRepositoryTest {

    @Test
    void should_return_transactions() throws TransactionReadException {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/goodTransactions.csv";
        TransactionRepository repository = new TransactionRepository(path);
        Transaction firstTransaction = Transaction.builder().name("transaction1").amount(300).date(LocalDateTime.parse("2023-03-15T13:14:15")).description("gold").type(DEBIT).build();
        Transaction secondTransaction = Transaction.builder().name("transaction2").amount(500).date(LocalDateTime.parse("2021-11-28T04:05:06")).description("silver").type(CREDIT).build();

        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).containsExactly(firstTransaction, secondTransaction);
    }

    @Test
    void should_not_return_transactions_when_file_is_empty() throws TransactionReadException {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/emptyFile.csv";
        TransactionRepository repository = new TransactionRepository(path);
        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).isEmpty();
    }

    @Test
    void should_throw_corrected_exception_when_amount_isNAN() {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/amountNan.csv";
        TransactionRepository repository = new TransactionRepository(path);
        TransactionReadException exception = assertThrows(TransactionReadException.class, repository::getTransactions);

        assertThat(exception).hasMessage("could not parse number in the transaction");
    }

    @Test
    void should_throw_correct_exception_when_type_is_credit_or_debit(){
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/unknownType.csv";
        TransactionRepository repository = new TransactionRepository(path);
        TransactionReadException exception = assertThrows(TransactionReadException.class, repository::getTransactions);

        assertThat(exception).hasMessage("could not parse transaction type");
    }
    /* @todo: update test exception
     * amount not empty
     * amount should be always > 0
     * type credit/debit only
     * handle extra spaces
     * dateFormat incorrect
     * ignore quotation
     * not enough columns
     * trailing white lines
     *  */
}