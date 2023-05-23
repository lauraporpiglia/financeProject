package com.mentoring.mentoringprj.service;

import com.mentoring.mentoringprj.domain.AccountDetails;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.domain.TransactionType;
import com.mentoring.mentoringprj.exceptions.AmountException;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.repository.TransactionRepository;
import com.mentoring.mentoringprj.util.TransactionCalculator;
import com.mentoring.mentoringprj.util.TransactionFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    private static final LocalDateTime DATE = LocalDateTime.now();
    @Mock
    private TransactionRepository repository;
    @Mock
    private TransactionFilter filter;


    @Test
    void should_return_transactions() throws TransactionReadException, AmountException {
        Transaction expectedTransaction = Transaction.builder().type(TransactionType.CREDIT).amount(300).build();
        when(repository.getTransactions()).thenReturn(List.of(expectedTransaction)); //remember this is a given not a when ARRANGE
        AccountService service = new AccountService(repository, new TransactionCalculator(), filter);
        //act
        AccountDetails accountDetails = service.getAccountDetails(Optional.empty(), Optional.empty());
        //then
        assertThat(accountDetails.getTransactions()).containsExactly(expectedTransaction);
        assertThat(accountDetails.getBalance()).isEqualTo(300);
    }

    @Test
    void should_return_filtered_transactions() throws TransactionReadException, AmountException {

        Transaction transaction1 = Transaction.builder().type(TransactionType.CREDIT).amount(300).build();
        Transaction transaction2 = Transaction.builder().type(TransactionType.CREDIT).amount(200).build();
        List<Transaction> transactions = List.of(transaction1, transaction2);
        when(repository.getTransactions()).thenReturn(transactions); //remember this is a given not a when ARRANGE
        when(filter.getTransactionsBetween( transactions, DATE,DATE)).thenReturn(List.of(transaction1));
        AccountService service = new AccountService(repository, new TransactionCalculator(), filter);
        //act
        AccountDetails accountDetails = service.getAccountDetails(Optional.of(DATE),Optional.of(DATE));

        //then
        assertThat(accountDetails.getTransactions()).containsExactly(transaction1);
        assertThat(accountDetails.getBalance()).isEqualTo(300);
    }

    @Test
    void should_return_transactions_filtered_by_date_from() throws TransactionReadException, AmountException {

        Transaction transaction1 = Transaction.builder().type(TransactionType.CREDIT).amount(300).build();
        Transaction transaction2 = Transaction.builder().type(TransactionType.CREDIT).amount(200).build();
        List<Transaction> transactions = List.of(transaction1, transaction2);
        when(repository.getTransactions()).thenReturn(transactions); //remember this is a given not a when ARRANGE
        when(filter.getTransactionsBetween( eq(transactions),eq(DATE),any())).thenReturn(List.of(transaction2));
        AccountService service = new AccountService(repository, new TransactionCalculator(), filter);
        //act
        AccountDetails accountDetails = service.getAccountDetails(Optional.of(DATE),Optional.empty());

        //then
        assertThat(accountDetails.getTransactions()).containsExactly(transaction2);
        assertThat(accountDetails.getBalance()).isEqualTo(200);
    }
    @Test
    void should_return_transactions_filtered_by_date_to() throws TransactionReadException, AmountException {
        Transaction transaction1 = Transaction.builder().type(TransactionType.CREDIT).amount(300).build();
        Transaction transaction2 = Transaction.builder().type(TransactionType.CREDIT).amount(200).build();
        List<Transaction> transactions = List.of(transaction1, transaction2);
        when(repository.getTransactions()).thenReturn(transactions);
        when(filter.getTransactionsBetween( eq(transactions), any(),eq(DATE))).thenReturn(List.of(transaction1,transaction2));
        AccountService service = new AccountService(repository, new TransactionCalculator(), filter);
        //act
        AccountDetails accountDetails = service.getAccountDetails(Optional.empty(),Optional.of(DATE));

        //then
        assertThat(accountDetails.getTransactions()).contains(transaction1,transaction2);
        assertThat(accountDetails.getBalance()).isEqualTo(500);
    }

    // filtering by 1 date (from AND to)
    //   - if only fromDate then set toDate to eijhcberlbrrfvtudhcrh
    //   - if only toDate then set from to start of time (LocalDateTime.MIN)....

}
