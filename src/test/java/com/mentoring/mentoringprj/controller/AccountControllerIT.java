package com.mentoring.mentoringprj.controller;

import com.mentoring.mentoringprj.domain.AccountDetails;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.domain.TransactionType;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerIT {
    @MockBean
    private AccountService accountService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void should_get_all_transactions() throws TransactionReadException {
        Transaction expectedTransaction = Transaction.builder().amount(300).type(TransactionType.CREDIT).build();
        List<Transaction> expectedTransactions = List.of(expectedTransaction);
        AccountDetails expectedAccountDetails = AccountDetails.builder().balance(300).transactions(expectedTransactions).build();
        when(accountService.getAccountDetails(Optional.empty(), Optional.empty())).thenReturn(expectedAccountDetails);

        ResponseEntity<AccountDetails> response = restTemplate.getForEntity("/account", AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(expectedAccountDetails);
    }

    @Test
    void should_get_all_transactions_filtered() throws TransactionReadException {
        LocalDateTime fromDate = LocalDateTime.of(2023, 11, 21, 13, 14);
        LocalDateTime toDate = LocalDateTime.of(2023, 11, 22, 14, 15);
        Transaction expectedTransaction = Transaction.builder().amount(300).type(TransactionType.CREDIT).build();
        List<Transaction> expectedTransactions = List.of(expectedTransaction);
        AccountDetails expectedAccountDetails = AccountDetails.builder().balance(300).transactions(expectedTransactions).build();
        when(accountService.getAccountDetails(Optional.of(fromDate), Optional.of(toDate))).thenReturn(expectedAccountDetails);

        String url = UriComponentsBuilder.fromPath("/account")
                .queryParam("from", fromDate.toString())
                .queryParam("to", toDate.toString())
                .toUriString();

        ResponseEntity<AccountDetails> response = restTemplate.getForEntity(url, AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(expectedAccountDetails);
    }
    @Test
    void should_add_transactions() throws TransactionReadException, IOException {
        Transaction existingTransaction = Transaction.builder().amount(300).type(TransactionType.CREDIT).build();
        Transaction newTransaction = Transaction.builder().amount(150).type(TransactionType.CREDIT).build();
        List<Transaction> expectedTransactions = List.of(existingTransaction,newTransaction);
        AccountDetails expectedAccountDetails = AccountDetails.builder().balance(450).transactions(expectedTransactions).build();
        when(accountService.addTransaction(newTransaction)).thenReturn(expectedAccountDetails);

        ResponseEntity<AccountDetails> response = restTemplate.postForEntity("/account", newTransaction, AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(expectedAccountDetails);
    }

    @Test
    void should_delete_a_transaction() throws TransactionReadException, IOException {
        Transaction transactionToKeep = Transaction.builder().id("1").amount(300).type(TransactionType.CREDIT).build();

        List<Transaction> expectedTransactions = Collections.singletonList(transactionToKeep);
        AccountDetails expectedAccountDetails = AccountDetails.builder().balance(300).transactions(expectedTransactions).build();

        when(accountService.delete("2")).thenReturn(expectedAccountDetails);
        ResponseEntity<AccountDetails> response = restTemplate.exchange("/account/2", HttpMethod.DELETE, HttpEntity.EMPTY, AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(expectedAccountDetails);
    }
}
