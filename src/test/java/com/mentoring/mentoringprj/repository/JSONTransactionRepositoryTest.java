package com.mentoring.mentoringprj.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.exceptions.AmountException;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

import static com.mentoring.mentoringprj.domain.TransactionType.CREDIT;
import static com.mentoring.mentoringprj.domain.TransactionType.DEBIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JSONTransactionRepositoryTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    @Test
    void should_return_transactions() throws TransactionReadException, AmountException {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/goodTransactions.json";
        JSONTransactionRepository repository = new JSONTransactionRepository(path,objectMapper);
        Transaction firstTransaction = Transaction.builder().name("transaction1").amount(300).date(LocalDateTime.parse("2023-03-15T13:14:15")).description("gold").type(DEBIT).build();
        Transaction secondTransaction = Transaction.builder().name("transaction2").amount(500).date(LocalDateTime.parse("2021-11-28T04:05:06")).description("silver").type(CREDIT).build();

        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).containsExactly(firstTransaction, secondTransaction);
    }

    @Test
    void should_not_return_an_empty_list_of_transactions_when_file_is_empty() throws TransactionReadException, AmountException {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/emptyFile.json";
        JSONTransactionRepository repository = new JSONTransactionRepository(path,objectMapper);
        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).isEmpty();
    }

    @Test
    void should_throw_corrected_exception_when_amount_isNAN() {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/amountNan.json";
        JSONTransactionRepository repository = new JSONTransactionRepository(path,objectMapper);
        TransactionReadException exception = assertThrows(TransactionReadException.class, repository::getTransactions);

        assertThat(exception).hasMessage("Data Type mismatch");
    }


    @Test
    void should_throw_correct_exception_when_type_is_not_credit_or_debit() {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/unknownType.json";
        JSONTransactionRepository repository = new JSONTransactionRepository(path,objectMapper );
        TransactionReadException exception = assertThrows(TransactionReadException.class, repository::getTransactions);

        assertThat(exception).hasMessage("Data Type mismatch");
    }


    @Test
    void should_throw_correct_exception_when_amount_is_zero_or_less() throws TransactionReadException, AmountException {
        String path = "/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/emptyAmount.json";
        JSONTransactionRepository repository = new JSONTransactionRepository(path,objectMapper);

        TransactionReadException exception = assertThrows(TransactionReadException.class, repository::getTransactions);

        assertThat(exception).hasMessage("Incorrect amount");
    }

    //create a empty dir
    //copy a known  file into it
    //run app against that file to read it
    //insert a new transaction in that file and save it.

    @Test
    void should_add_a_transaction() throws IOException, TransactionReadException {
        //given
        Path originalPath = Path.of("/Users/lauraporpiglia/rides/mentoring/mentoringPrj/src/test/resources/transactions/goodTransactions.json");
        Path newPath = Path.of("/Users/lauraporpiglia/rides/mentoring/mentoringPrj/build/testout/goodTransactions.json");
        newPath.toFile().getParentFile().mkdirs();
        Files.copy(originalPath, newPath, StandardCopyOption.REPLACE_EXISTING);

        JSONTransactionRepository repository = new JSONTransactionRepository(newPath.toString(),objectMapper);
        Transaction firstTransaction = Transaction.builder().name("transaction1").amount(300).date(LocalDateTime.parse("2023-03-15T13:14:15")).description("gold").type(DEBIT).build();
        Transaction secondTransaction = Transaction.builder().name("transaction2").amount(500).date(LocalDateTime.parse("2021-11-28T04:05:06")).description("silver").type(CREDIT).build();
        Transaction newTransaction = Transaction.builder().name("newTransaction").amount(123).type(DEBIT).build();
        //when
        repository.addTransaction(newTransaction);

        //then
        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).containsExactly(firstTransaction, secondTransaction, newTransaction);


    }


}
