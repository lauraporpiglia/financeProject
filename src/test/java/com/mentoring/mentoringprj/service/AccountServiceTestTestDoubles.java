package com.mentoring.mentoringprj.service;

import com.mentoring.mentoringprj.domain.AccountDetails;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.domain.TransactionType;
import com.mentoring.mentoringprj.domain.TransactionWithoutId;
import com.mentoring.mentoringprj.exceptions.AmountException;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.repository.TransactionRepository;
import com.mentoring.mentoringprj.util.LocalDateTimeProvider;
import com.mentoring.mentoringprj.util.TransactionCalculator;
import com.mentoring.mentoringprj.util.TransactionFilter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Disabled
class AccountServiceTestTestDoubles {
    private static final LocalDateTime DATE = LocalDateTime.now();
    @Mock
    private StubTransactionRepository repository;
    @Mock
    private TransactionFilter filter;
    @Mock
    private LocalDateTimeProvider localTimeProvider;

    private AccountService subject;


    private void setUp(List<Transaction> transactionsToReturn) {
        repository = new StubTransactionRepository(transactionsToReturn);
        subject = new AccountService(repository, new TransactionCalculator(), filter, localTimeProvider);
    }

    @Test
    void should_return_transactions_unfiltered() throws TransactionReadException, AmountException {
        Transaction expectedTransaction = Transaction.builder().type(TransactionType.CREDIT).amount(300).build();
        setUp(List.of(expectedTransaction));

        //act
        AccountDetails accountDetails = subject.getAccountDetails(Optional.empty(), Optional.empty());
        //then
        assertThat(accountDetails.getTransactions()).containsExactly(expectedTransaction);
        assertThat(accountDetails.getBalance()).isEqualTo(300);
    }

    @Test
    void should_return_transactions() throws TransactionReadException, AmountException {
        Transaction expectedTransaction = Transaction.builder().type(TransactionType.CREDIT).amount(300).build();
        setUp(List.of(expectedTransaction));
        when(repository.getTransactions()).thenReturn(List.of(expectedTransaction)); //remember this is a given not a when ARRANGE

        //act
        AccountDetails accountDetails = subject.getAccountDetails();
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
        when(filter.getTransactionsBetween(transactions, DATE, DATE)).thenReturn(List.of(transaction1));

        //act
        AccountDetails accountDetails = subject.getAccountDetails(Optional.of(DATE), Optional.of(DATE));

        //then
        assertThat(accountDetails.getTransactions()).containsExactly(transaction1);
        assertThat(accountDetails.getBalance()).isEqualTo(300);
    }

    @Test
    void should_return_transactions_filtered_by_date_from() throws TransactionReadException, AmountException {
        LocalDateTime toDate = LocalDateTime.now();

        Transaction transaction1 = Transaction.builder().type(TransactionType.CREDIT).amount(300).build();
        Transaction transaction2 = Transaction.builder().type(TransactionType.CREDIT).amount(200).build();
        List<Transaction> transactions = List.of(transaction1, transaction2);
        when(localTimeProvider.now()).thenReturn(toDate);
        when(repository.getTransactions()).thenReturn(transactions); //remember this is a given not a when ARRANGE
        when(filter.getTransactionsBetween(transactions, DATE, toDate)).thenReturn(List.of(transaction2));

        //act
        AccountDetails accountDetails = subject.getAccountDetails(Optional.of(DATE), Optional.empty());

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
        when(filter.getTransactionsBetween(transactions, LocalDateTime.MIN, DATE)).thenReturn(List.of(transaction1, transaction2));

        //act
        AccountDetails accountDetails = subject.getAccountDetails(Optional.empty(), Optional.of(DATE));

        //then
        assertThat(accountDetails.getTransactions()).contains(transaction1, transaction2);
        assertThat(accountDetails.getBalance()).isEqualTo(500);
    }

    @Test
    void should_call_repository_correctly_when_adding_a_transaction() throws Exception {
        //given
        TransactionWithoutId newTransactionNoId = TransactionWithoutId.builder().type(TransactionType.CREDIT).amount(200).build();
        Transaction newTransaction = newTransactionNoId.toNewTransaction();

        //when
        subject.addTransaction(newTransactionNoId);

        //then
        InOrder inOrder = inOrder(repository);
        inOrder.verify(repository).addTransaction(newTransaction); //remember verify just check interactions
        inOrder.verify(repository).getTransactions();
    }

    @Test
    void should_return_correct_results_when_adding_transaction() throws Exception {
        //given
        Transaction existingTransaction = Transaction.builder().type(TransactionType.CREDIT).amount(300).build();
        TransactionWithoutId newTransactionNoId = TransactionWithoutId.builder().type(TransactionType.CREDIT).amount(200).build();
        Transaction newTransaction = newTransactionNoId.toNewTransaction();
        setUp(List.of(existingTransaction, newTransaction));

        //when
        AccountDetails accountDetails = subject.addTransaction(newTransactionNoId);
        // accountDetails = service.addTransaction(newTransaction);

        //then
        assertThat(repository.getAddedTransactions()).hasSize(1);
        assertThat(repository.getAddedTransactions()).containsExactly(newTransaction);
        assertThat(accountDetails.getTransactions()).containsExactly(existingTransaction, newTransaction);
        assertThat(accountDetails.getBalance()).isEqualTo(500);
    }

    @Test
    void should_return_correct_results_when_deleting_transaction() throws Exception {
        //given
        Transaction remainingTransaction = Transaction.builder().id("1").type(TransactionType.CREDIT).amount(300).build();
       setUp(List.of(remainingTransaction));

        //when
        AccountDetails accountDetails = subject.delete("2");

        //then
        assertThat(repository.getDeletedTransactions()).hasSize(1);
        assertThat(repository.getDeletedTransactions()).containsExactly("2");
        assertThat(accountDetails.getTransactions()).containsExactly(remainingTransaction);
        assertThat(accountDetails.getBalance()).isEqualTo(300);
    }

    @Test
    void should_call_repository_correctly_when_deleting_a_transaction() throws Exception {
        //given
        Transaction remainingTransaction = Transaction.builder().id("1").type(TransactionType.CREDIT).amount(300).build();
        when(repository.getTransactions()).thenReturn(List.of(remainingTransaction));

        //when
        AccountDetails accountDetails = subject.delete("2");
        //then
        InOrder inOrder = inOrder(repository);
        inOrder.verify(repository).deleteTransaction("2"); //remember verify just check interactions
        inOrder.verify(repository).getTransactions();
    }

    private static class StubTransactionRepository implements TransactionRepository {
        private final List<Transaction> transactionsToReturn;


        private final List<Transaction> addedTransactions;
        private final List<String> deletedTransactions;

        public StubTransactionRepository(List<Transaction> transactionsToReturn) {
            this.transactionsToReturn = transactionsToReturn;
            addedTransactions = new ArrayList<>();
            deletedTransactions = new ArrayList<>();
        }

        @Override
        public List<Transaction> getTransactions() throws TransactionReadException {
            return transactionsToReturn;
        }

        @Override
        public List<Transaction> getTransactionsById(String transactionId) throws TransactionReadException {
            return null;
        }

        @Override
        public void addTransaction(Transaction transaction) throws TransactionReadException, IOException {
            addedTransactions.add(transaction);
        }

        @Override
        public void deleteTransaction(String id) throws TransactionReadException, IOException {
            deletedTransactions.add(id);
        }

        @Override
        public void updateTransaction(Transaction transaction) throws TransactionReadException, IOException {

        }

        public List<Transaction> getAddedTransactions() {
            return addedTransactions;
        }
        public List<String> getDeletedTransactions() {
            return deletedTransactions;
        }
    }


}
