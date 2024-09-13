package com.finance.financedashboard.controller;

import com.finance.financedashboard.domain.AccountDetails;
import com.finance.financedashboard.domain.Transaction;
import com.finance.financedashboard.domain.TransactionWithoutId;
import com.finance.financedashboard.domain.TransactionType;
import com.finance.financedashboard.exceptions.TransactionNotFoundException;
import com.finance.financedashboard.exceptions.TransactionReadException;
import com.finance.financedashboard.repository.TransactionRepository;
import com.finance.financedashboard.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerIT {
    private static final String TRANSACTION_ID_1 = "2bf372d1-7379-42df-816a-c09614904f33";
    private static final String TRANSACTION_ID_2 = "3bf372d1-7379-42df-816a-c09614904f33";
    @Autowired
    private AccountService accountService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TransactionRepository repository;

    private final String UNEXISTENT_ID = "UNEXISTENT_ID";

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }

    @Test
    void should_get_all_transactions() {
        Transaction expectedTransaction = Transaction.builder().id("1").amount(300).type(TransactionType.CREDIT).build();
        List<Transaction> expectedTransactions = List.of(expectedTransaction);
        AccountDetails expectedAccountDetails = AccountDetails.builder().balance(300).transactions(expectedTransactions).build();
        repository.save(expectedTransaction.toTransactionEntity());

        ResponseEntity<AccountDetails> response = restTemplate.getForEntity("/account", AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedAccountDetails);
    }

    @Test
    void should_get_a_single_transaction() {
        //given
        Transaction expectedTransaction = Transaction.builder().id("1").amount(300).type(TransactionType.CREDIT).build();
        repository.save(expectedTransaction.toTransactionEntity());
         //when
        ResponseEntity<Transaction> response = restTemplate.getForEntity("/account/1", Transaction.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedTransaction);
    }

    @Test
    void should_return404_when_retrieving_a_nonExistent_Single_transaction() {
        ResponseEntity<Transaction> response = restTemplate.getForEntity("/account/1", Transaction.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void should_get_all_transactions_filtered_byDate() {
        LocalDateTime fromDate = LocalDateTime.of(2023, 11, 21, 13, 14);
        LocalDateTime toDate = LocalDateTime.of(2023, 11, 22, 14, 15);
        Transaction expectedTransaction = Transaction.builder().id("1").amount(300).type(TransactionType.CREDIT).date(toDate).build();
        List<Transaction> expectedTransactions = List.of(expectedTransaction);
        AccountDetails expectedAccountDetails = AccountDetails.builder().balance(300).transactions(expectedTransactions).build();
        repository.save(expectedTransaction.toTransactionEntity());

        String url = UriComponentsBuilder.fromPath("/account")
                .queryParam("from", fromDate.toString())
                .queryParam("to", toDate.toString())
                .toUriString();

        ResponseEntity<AccountDetails> response = restTemplate.getForEntity(url, AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedAccountDetails);
    }

    @Test
    void should_add_transactions() {
        TransactionWithoutId newTransactionWithoutId = TransactionWithoutId.builder().amount(150).type(TransactionType.CREDIT).build();
        Transaction existingTransaction = Transaction.builder().id("1").amount(300).type(TransactionType.CREDIT).build();
        repository.save(existingTransaction.toTransactionEntity());

        ResponseEntity<AccountDetails> response = restTemplate.postForEntity("/account", newTransactionWithoutId, AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBalance()).isEqualTo(450);
        assertThat(response.getBody().getTransactions().get(0)).isEqualTo(existingTransaction);
        assertThat(response.getBody().getTransactions().get(1).getId()).isNotNull();
        assertThat(response.getBody().getTransactions().get(1).getAmount()).isEqualTo(newTransactionWithoutId.getAmount());
        assertThat(response.getBody().getTransactions().get(1).getType()).isEqualTo(newTransactionWithoutId.getType());
    }

    @Test
    void should_delete_a_transaction() throws TransactionReadException, IOException, TransactionNotFoundException {
        //given
        Transaction transactionToKeep = Transaction.builder().id(TRANSACTION_ID_1).amount(300).type(TransactionType.CREDIT).build();
        Transaction transactionToDelete = Transaction.builder().id(TRANSACTION_ID_2).amount(100).type(TransactionType.CREDIT).build();
        List<Transaction> expectedTransactions = Collections.singletonList(transactionToKeep);
        AccountDetails expectedAccountDetails = AccountDetails.builder().balance(300).transactions(expectedTransactions).build();
        repository.saveAll(List.of(transactionToKeep.toTransactionEntity(), transactionToDelete.toTransactionEntity()));
        //when
        ResponseEntity<AccountDetails> response = restTemplate.exchange("/account/%s".formatted(TRANSACTION_ID_2), HttpMethod.DELETE, HttpEntity.EMPTY, AccountDetails.class);
//then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedAccountDetails);
    }

    @Test
    void should_update_a_transaction() {
        Transaction existingTransaction = Transaction.builder().id(TRANSACTION_ID_1).amount(300).type(TransactionType.CREDIT).build();
        Transaction transactionToUpdate = Transaction.builder().id(TRANSACTION_ID_2).amount(150).type(TransactionType.CREDIT).build();
        Transaction updateTransaction = Transaction.builder().id(TRANSACTION_ID_2).amount(200).type(TransactionType.CREDIT).build();

        List<Transaction> expectedTransactions = List.of(existingTransaction, updateTransaction);

        AccountDetails expectedAccountDetails = AccountDetails.builder().balance(500).transactions(expectedTransactions).build();
        repository.saveAll(List.of(existingTransaction.toTransactionEntity(), transactionToUpdate.toTransactionEntity()));
        RequestEntity<TransactionWithoutId> request = RequestEntity
                .put("/account/update/{transactionId}", transactionToUpdate.getId())
                .body(TransactionWithoutId.builder().amount(updateTransaction.getAmount()).type(updateTransaction.getType()).build());


        ResponseEntity<AccountDetails> response = restTemplate.exchange(request, AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedAccountDetails);
    }

    @Test
    void should_return404_when_delete_a_nonexistent_transaction() {
        ResponseEntity<AccountDetails> response = restTemplate.exchange("/account/%s".formatted(UNEXISTENT_ID), HttpMethod.DELETE, HttpEntity.EMPTY, AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
