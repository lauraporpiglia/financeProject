package com.mentoring.mentoringprj.controller;

import com.mentoring.mentoringprj.domain.AccountDetails;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.domain.TransactionWithoutId;
import com.mentoring.mentoringprj.domain.TransactionType;
import com.mentoring.mentoringprj.exceptions.TransactionNotFoundException;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
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

    private final String UNEXISTENT_ID = "UNEXISTENT_ID";

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
    void should_get_all_transactions_filtered_byDate() throws TransactionReadException {
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
        TransactionWithoutId newTransactionWithoutId = TransactionWithoutId.builder().amount(150).type(TransactionType.CREDIT).build();
        Transaction newTransaction = newTransactionWithoutId.toNewTransaction();
        Transaction existingTransaction = Transaction.builder().amount(300).type(TransactionType.CREDIT).build();
        List<Transaction> expectedTransactions = List.of(existingTransaction,newTransaction);
        AccountDetails expectedAccountDetails = AccountDetails.builder().balance(450).transactions(expectedTransactions).build();
        when(accountService.addTransaction(newTransactionWithoutId)).thenReturn(expectedAccountDetails);

        ResponseEntity<AccountDetails> response = restTemplate.postForEntity("/account", newTransactionWithoutId, AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(expectedAccountDetails);
    }

    @Test
    void should_delete_a_transaction() throws TransactionReadException, IOException, TransactionNotFoundException {
        Transaction transactionToKeep = Transaction.builder().id("1").amount(300).type(TransactionType.CREDIT).build();

        List<Transaction> expectedTransactions = Collections.singletonList(transactionToKeep);
        AccountDetails expectedAccountDetails = AccountDetails.builder().balance(300).transactions(expectedTransactions).build();

        when(accountService.delete("2")).thenReturn(expectedAccountDetails);
        ResponseEntity<AccountDetails> response = restTemplate.exchange("/account/2", HttpMethod.DELETE, HttpEntity.EMPTY, AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(expectedAccountDetails);
    }
    @Test
    void should_update_a_transaction() throws TransactionReadException, IOException, TransactionNotFoundException {
        Transaction existingTransaction = Transaction.builder().id("1").amount(300).type(TransactionType.CREDIT).build();
        Transaction transactionToUpdate = Transaction.builder().id("2").amount(150).type(TransactionType.CREDIT).build();
        List<Transaction> expectedTransactions = List.of(existingTransaction,transactionToUpdate);
        TransactionWithoutId updateTransaction = TransactionWithoutId.builder().amount(150).type(TransactionType.CREDIT).build();

        AccountDetails expectedAccountDetails = AccountDetails.builder().balance(450).transactions(expectedTransactions).build();
        when(accountService.updateTransaction(transactionToUpdate.getId(),updateTransaction)).thenReturn(expectedAccountDetails);
        RequestEntity<TransactionWithoutId> request = RequestEntity
                .put("/account/update/{transactionId}",transactionToUpdate.getId())
                .body(updateTransaction);

        ResponseEntity<AccountDetails> response = restTemplate.exchange(request, AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(expectedAccountDetails);
    }

    @Test
    void should_throw_exception_when_delete_an_unexistent_transaction() throws Exception {
        when(accountService.delete(UNEXISTENT_ID)).thenThrow(new TransactionNotFoundException(""));
        ResponseEntity<AccountDetails> response = restTemplate.exchange("/account/%s".formatted(UNEXISTENT_ID), HttpMethod.DELETE, HttpEntity.EMPTY, AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
    }
    @Test
    void should_throw_exception_when_update_an_unexistent_transaction() throws Exception {
        TransactionWithoutId updateTransaction = TransactionWithoutId.builder().amount(150).type(TransactionType.CREDIT).build();
        when(accountService.updateTransaction(UNEXISTENT_ID,updateTransaction)).thenThrow(new TransactionNotFoundException(""));
        ResponseEntity<AccountDetails> response = restTemplate.exchange("/update/%s".formatted(UNEXISTENT_ID), HttpMethod.PUT, HttpEntity.EMPTY, AccountDetails.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
    }

}
