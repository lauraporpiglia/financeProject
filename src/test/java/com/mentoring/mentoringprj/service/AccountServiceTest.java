package com.mentoring.mentoringprj.service;

import com.mentoring.mentoringprj.domain.AccountDetails;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.domain.TransactionType;
import com.mentoring.mentoringprj.domain.TransactionWithoutId;
import com.mentoring.mentoringprj.exceptions.AmountException;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.repository.TransactionRepository;
import com.mentoring.mentoringprj.repository.entity.TransactionEntity;
import com.mentoring.mentoringprj.util.LocalDateTimeProvider;
import com.mentoring.mentoringprj.util.TransactionCalculator;
import com.mentoring.mentoringprj.util.TransactionFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    private static final LocalDateTime DATE = LocalDateTime.now();
    @Mock
    private TransactionRepository repository;
    @Mock
    private TransactionFilter filter;

    @Mock
    private LocalDateTimeProvider localTimeProvider;
    @Captor
    private ArgumentCaptor<TransactionEntity> transactionCaptor;
    private AccountService service;

    @BeforeEach
    void setUp() {
        service = new AccountService(repository, new TransactionCalculator(), filter, localTimeProvider);
    }

    @Test
    void should_return_transactions_unfiltered() throws TransactionReadException, AmountException {
        TransactionEntity expectedTransaction = TransactionEntity.builder().type(String.valueOf(TransactionType.CREDIT)).amount(300).build();
        when(repository.findAll()).thenReturn(List.of(expectedTransaction)); //remember this is a given not a when ARRANGE

        //act
        AccountDetails accountDetails = service.getAccountDetails(Optional.empty(), Optional.empty());
        //then
        assertThat(accountDetails.getTransactions()).containsExactly(expectedTransaction.toTransaction());
        assertThat(accountDetails.getBalance()).isEqualTo(300);
    }

    @Test
    void should_return_a_single_transaction_byId() {
        TransactionEntity expectedTransaction = TransactionEntity.builder().type(String.valueOf(TransactionType.CREDIT)).id("123").amount(300).build();
        when(repository.findById("123")).thenReturn(Optional.of(expectedTransaction));

        Optional<Transaction> transaction = service.getTransaction("123");
        assertThat(transaction).isPresent();
        assertThat(transaction.get()).isEqualTo(expectedTransaction.toTransaction());
    }

    @Test
    void should_return_an_emptyOptional_whenTransactionById_notFound() {

        Optional<Transaction> transaction = service.getTransaction("123");

        assertThat(transaction).isNotPresent();
    }

    @Test
    void should_return_transactions() throws TransactionReadException, AmountException {
        TransactionEntity expectedTransaction = TransactionEntity.builder().type(String.valueOf(TransactionType.CREDIT)).amount(300).build();
        when(repository.findAll()).thenReturn(List.of(expectedTransaction)); //remember this is a given not a when ARRANGE

        //act
        AccountDetails accountDetails = service.getAccountDetails();
        //then
        assertThat(accountDetails.getTransactions()).containsExactly(expectedTransaction.toTransaction());
        assertThat(accountDetails.getBalance()).isEqualTo(300);
    }

    @Test
    void should_return_filtered_transactions() throws TransactionReadException, AmountException {

        TransactionEntity transaction1 = TransactionEntity.builder().type(TransactionType.CREDIT.toString()).amount(300).build();
        TransactionEntity transaction2 = TransactionEntity.builder().type(TransactionType.CREDIT.toString()).amount(200).build();
        List<TransactionEntity> transactionEntities = List.of(transaction1, transaction2);
        List<Transaction> transactions = List.of(transaction1.toTransaction(), transaction2.toTransaction());
        when(repository.findAll()).thenReturn(transactionEntities); //remember this is a given not a when ARRANGE
        when(filter.getTransactionsBetween(transactions, DATE, DATE)).thenReturn(List.of(transaction1.toTransaction()));

        //act
        AccountDetails accountDetails = service.getAccountDetails(Optional.of(DATE), Optional.of(DATE));

        //then
        assertThat(accountDetails.getTransactions()).containsExactly(transaction1.toTransaction());
        assertThat(accountDetails.getBalance()).isEqualTo(300);
    }

    @Test
    void should_return_transactions_filtered_by_date_from() throws TransactionReadException, AmountException {
        LocalDateTime toDate = LocalDateTime.now();

        TransactionEntity transaction1 = TransactionEntity.builder().type(TransactionType.CREDIT.toString()).amount(300).build();
        TransactionEntity transaction2 = TransactionEntity.builder().type(TransactionType.CREDIT.toString()).amount(200).build();
        List<TransactionEntity> transactionEntities = List.of(transaction1, transaction2);
        List<Transaction> transactions = List.of(transaction1.toTransaction(), transaction2.toTransaction());
        when(localTimeProvider.now()).thenReturn(toDate);
        when(repository.findAll()).thenReturn(transactionEntities); //remember this is a given not a when ARRANGE
        when(filter.getTransactionsBetween(transactions, DATE, toDate)).thenReturn(List.of(transaction2.toTransaction()));

        //act
        AccountDetails accountDetails = service.getAccountDetails(Optional.of(DATE), Optional.empty());

        //then
        assertThat(accountDetails.getTransactions()).containsExactly(transaction2.toTransaction());
        assertThat(accountDetails.getBalance()).isEqualTo(200);
    }

    @Test
    void should_return_transactions_filtered_by_date_to() throws TransactionReadException, AmountException {
        TransactionEntity transaction1 = TransactionEntity.builder().type(TransactionType.CREDIT.toString()).amount(300).build();
        TransactionEntity transaction2 = TransactionEntity.builder().type(TransactionType.CREDIT.toString()).amount(200).build();
        List<TransactionEntity> transactionEntities = List.of(transaction1, transaction2);
        List<Transaction> transactions = List.of(transaction1.toTransaction(), transaction2.toTransaction());
        when(repository.findAll()).thenReturn(transactionEntities);
        when(filter.getTransactionsBetween(transactions, LocalDateTime.MIN, DATE)).thenReturn(List.of(transaction1.toTransaction(), transaction2.toTransaction()));

        //act
        AccountDetails accountDetails = service.getAccountDetails(Optional.empty(), Optional.of(DATE));

        //then
        assertThat(accountDetails.getTransactions()).contains(transaction1.toTransaction(), transaction2.toTransaction());
        assertThat(accountDetails.getBalance()).isEqualTo(500);
    }

    @Test
    void should_call_repository_correctly_when_saving_a_transaction() throws Exception {
        //given

        TransactionWithoutId newTransactionWithoutId = TransactionWithoutId.builder().type(TransactionType.CREDIT).amount(200).build();
        //when
        service.saveTransaction(newTransactionWithoutId);

        //then
        InOrder inOrder = inOrder(repository);
        inOrder.verify(repository).save(transactionCaptor.capture()); //remember verify just check interactions
        inOrder.verify(repository).findAll();
        TransactionEntity addedTransaction = transactionCaptor.getValue();
        assertThat(addedTransaction.getType()).isEqualTo(newTransactionWithoutId.getType().toString());
        assertThat(addedTransaction.getAmount()).isEqualTo(newTransactionWithoutId.getAmount());
        assertThat(addedTransaction.getId()).isNotNull();
    }

    @Test
    void should_call_repository_correctly_when_saving_a_transactionWithId() throws Exception {
        //given

        TransactionWithoutId newTransactionWithoutId = TransactionWithoutId.builder().type(TransactionType.CREDIT).amount(200).build();
        //when
        service.saveTransaction("id", newTransactionWithoutId);

        //then
        InOrder inOrder = inOrder(repository);
        inOrder.verify(repository).save(transactionCaptor.capture()); //remember verify just check interactions
        inOrder.verify(repository).findAll();
        TransactionEntity addedTransaction = transactionCaptor.getValue();
        assertThat(addedTransaction.getType()).isEqualTo(newTransactionWithoutId.getType().toString());
        assertThat(addedTransaction.getAmount()).isEqualTo(newTransactionWithoutId.getAmount());
        assertThat(addedTransaction.getId()).isEqualTo("id");
    }

    @Test
    void should_return_correct_results_when_saving_transaction() throws Exception {
        //given
        TransactionEntity existingTransaction = TransactionEntity.builder().type(TransactionType.CREDIT.toString()).amount(300).build();
        TransactionWithoutId newTransactionWithoutId = TransactionWithoutId.builder().type(TransactionType.CREDIT).amount(200).build();
        TransactionEntity newTransaction = newTransactionWithoutId.toNewTransaction().toTransactionEntity();
        when(repository.findAll()).thenReturn(List.of(existingTransaction, newTransaction));

        //when
        AccountDetails accountDetails = service.saveTransaction(newTransactionWithoutId);

        //then
        verify(repository).save(transactionCaptor.capture()); //remember verify just check interactions
        TransactionEntity addedTransaction = transactionCaptor.getValue();
        assertThat(addedTransaction.getType()).isEqualTo(newTransactionWithoutId.getType().toString());
        assertThat(addedTransaction.getAmount()).isEqualTo(newTransactionWithoutId.getAmount());
        assertThat(addedTransaction.getId()).isNotNull();

        assertThat(accountDetails.getTransactions()).containsExactly(existingTransaction.toTransaction(), newTransaction.toTransaction());
        assertThat(accountDetails.getBalance()).isEqualTo(500);
    }

    @Test
    void should_return_correct_results_when_deleting_transaction() throws Exception {
        //given
        TransactionEntity remainingTransaction = TransactionEntity.builder().id("1").type(TransactionType.CREDIT.toString()).amount(300).build();
        when(repository.findAll()).thenReturn(List.of(remainingTransaction));

        //when
        AccountDetails accountDetails = service.delete("2");

        //then
        verify(repository).deleteById("2"); //remember verify just check interactions
        assertThat(accountDetails.getTransactions()).containsExactly(remainingTransaction.toTransaction());
        assertThat(accountDetails.getBalance()).isEqualTo(300);
    }

    @Test
    void should_call_repository_correctly_when_deleting_a_transaction() throws Exception {
        //given
        TransactionEntity remainingTransaction = TransactionEntity.builder().id("1").type(TransactionType.CREDIT.toString()).amount(300).build();
        when(repository.findAll()).thenReturn(List.of(remainingTransaction));

        //when
        AccountDetails accountDetails = service.delete("2");
        //then
        InOrder inOrder = inOrder(repository);
        inOrder.verify(repository).deleteById("2"); //remember verify just check interactions
        inOrder.verify(repository).findAll();
    }


}

