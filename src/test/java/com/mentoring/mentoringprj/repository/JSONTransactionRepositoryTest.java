package com.mentoring.mentoringprj.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.exceptions.TransactionNotFoundException;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.mentoring.mentoringprj.domain.TransactionType.CREDIT;
import static com.mentoring.mentoringprj.domain.TransactionType.DEBIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JSONTransactionRepositoryTest {
    private static final String SRC_PATH = "./src/test/resources/transactions/";
    private static final String BUILD_PATH = "./build/testout/";
    private ObjectMapper objectMapper;
    private static final String UNEXISTENT_TRANSACTION_ID = "UNEXISTENT_TRANSACTION_ID";

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    @Test
    void should_return_transactions() throws Exception {
        String file = SRC_PATH.concat("goodTransactions.json");

        JSONTransactionRepository repository = new JSONTransactionRepository(file, objectMapper);
        Transaction firstTransaction = Transaction.builder().id("1").name("transaction1").amount(300).date(LocalDateTime.parse("2023-03-15T13:14:15")).description("gold").type(DEBIT).build();
        Transaction secondTransaction = Transaction.builder().id("2").name("transaction2").amount(500).date(LocalDateTime.parse("2021-11-28T04:05:06")).description("silver").type(CREDIT).build();

        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).containsExactly(firstTransaction, secondTransaction);
    }

    @Test
    void should_not_return_an_empty_list_of_transactions_when_file_is_empty() throws Exception {
        String file = SRC_PATH.concat("emptyFile.json");
        JSONTransactionRepository repository = new JSONTransactionRepository(file, objectMapper);
        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideExceptionsScenarios")
    void should_Throw_TransactionReadException(String file, String exceptionMessage) {

        String fullPath = SRC_PATH.concat(file);
        JSONTransactionRepository repository = new JSONTransactionRepository(fullPath, objectMapper);
        Exception exception = assertThrows(TransactionReadException.class, repository::getTransactions);

        assertThat(exception).hasMessage(exceptionMessage);

    }

    //create a empty dir
    //copy a known  file into it
    //run app against that file to read it
    //insert a new transaction in that file and save it.

    @Test
    void should_add_a_transaction() throws Exception {
        //given
        String originalFile = SRC_PATH.concat("goodTransactions.json");
        String newFile = BUILD_PATH.concat("goodTransactions.json");

        copyFile(originalFile, newFile);

        JSONTransactionRepository repository = new JSONTransactionRepository(newFile, objectMapper);
        Transaction firstTransaction = Transaction.builder().id("1").name("transaction1").amount(300).date(LocalDateTime.parse("2023-03-15T13:14:15")).description("gold").type(DEBIT).build();
        Transaction secondTransaction = Transaction.builder().id("2").name("transaction2").amount(500).date(LocalDateTime.parse("2021-11-28T04:05:06")).description("silver").type(CREDIT).build();
        Transaction newTransaction = Transaction.builder().id("3").name("newTransaction").amount(123).type(DEBIT).build();
        //when
        repository.addTransaction(newTransaction);

        //then
        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).containsExactly(firstTransaction, secondTransaction, newTransaction);
    }

    @Test
    void should_add_a_transactionWithoutId() throws Exception {
        //given
        String originalFile = SRC_PATH.concat("goodTransactions.json");
        String newFile = BUILD_PATH.concat("goodTransactions.json");

        copyFile(originalFile, newFile);

        JSONTransactionRepository repository = new JSONTransactionRepository(newFile, objectMapper);
        Transaction firstTransaction = Transaction.builder().id("1").name("transaction1").amount(300).date(LocalDateTime.parse("2023-03-15T13:14:15")).description("gold").type(DEBIT).build();
        Transaction secondTransaction = Transaction.builder().id("2").name("transaction2").amount(500).date(LocalDateTime.parse("2021-11-28T04:05:06")).description("silver").type(CREDIT).build();

        Transaction newTransaction = Transaction.builder().name("newTransaction").amount(123).type(DEBIT).build();
        Transaction savedNewTransaction = Transaction.builder().name(newTransaction.getName()).amount(newTransaction.getAmount()).type(newTransaction.getType()).build();
        //when
        repository.addTransaction(newTransaction);

        //then
        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).hasSize(3)
                .contains(firstTransaction, secondTransaction);
        assertThat(transactions)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(savedNewTransaction);
    }

    @Test
    void should_throw_exception_when_delete_called_on_unexistent_transaction() throws Exception {
        //given
        String originalPath = SRC_PATH.concat("deletableTransactions.json");
        String newPath = BUILD_PATH.concat("deletableTransactions.json");
        copyFile(originalPath, newPath);

        JSONTransactionRepository repository = new JSONTransactionRepository(newPath, objectMapper);
        //when
        assertThrows(TransactionNotFoundException.class, () -> repository.deleteTransaction(UNEXISTENT_TRANSACTION_ID));
    }

    @Test
    void should_delete_a_transaction() throws Exception {
        //given
        String originalPath = SRC_PATH.concat("deletableTransactions.json");
        String newPath = BUILD_PATH.concat("deletableTransactions.json");
        copyFile(originalPath, newPath);

        JSONTransactionRepository repository = new JSONTransactionRepository(newPath, objectMapper);
        Transaction firstTransaction = Transaction.builder().id("1").name("transaction1").amount(300).date(LocalDateTime.parse("2023-03-15T13:14:15")).description("gold").type(DEBIT).build();
        //when
        repository.deleteTransaction("2");

        //then
        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).containsExactly(firstTransaction);
    }

    @Test
    void should_update_a_single_transaction() throws Exception {
        String originalFile = SRC_PATH.concat("updatedTransactions.json");
        String newFile = BUILD_PATH.concat("updatedTransactions.json");

        copyFile(originalFile, newFile);
        JSONTransactionRepository repository = new JSONTransactionRepository(newFile, objectMapper);

        Transaction firstTransaction = Transaction.builder().id("1").name("transaction1").amount(300).date(LocalDateTime.parse("2023-03-15T13:14:15")).description("gold").type(DEBIT).build();
        Transaction newTransaction = Transaction.builder().id("2").name("transaction_updated_2").amount(500).date(LocalDateTime.parse("2021-11-28T04:05:06")).description("silver").type(CREDIT).build();

        repository.updateTransaction(newTransaction);

        List<Transaction> transactions = repository.getTransactions();
        assertThat(transactions).containsExactly(firstTransaction, newTransaction);
    }

    @Test
    void should_throw_exception_when_updated_called_on_unexistent_transaction() throws Exception {
        String originalFile = SRC_PATH.concat("updatedTransactions.json");
        String newFile = BUILD_PATH.concat("updatedTransactions.json");

        copyFile(originalFile, newFile);
        JSONTransactionRepository repository = new JSONTransactionRepository(newFile, objectMapper);

        Transaction newTransaction = Transaction.builder().id(UNEXISTENT_TRANSACTION_ID).build();

        assertThrows(TransactionNotFoundException.class, () -> repository.updateTransaction(newTransaction));
    }

    private static void copyFile(String originalLocation, String newLocation) throws IOException {
        Path originalPath = Path.of(originalLocation);
        Path newPath = Path.of(newLocation);

        newPath.toFile().getParentFile().mkdirs();
        Files.copy(originalPath, newPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private static Stream<Arguments> provideExceptionsScenarios() {

        return Stream.of(
                Arguments.of("amountNan.json", "Data Type mismatch"),
                Arguments.of("unknownType.json", "Data Type mismatch"),
                Arguments.of("emptyAmount.json", "Incorrect amount")
        );
    }


}