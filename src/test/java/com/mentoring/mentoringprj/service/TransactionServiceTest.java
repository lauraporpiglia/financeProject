package com.mentoring.mentoringprj.service;

import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository repository;

    @Test
    void should_return_transactions() throws TransactionReadException {
        Transaction expectedTransaction =  Transaction.builder().build();
        when(repository.getTransactions()).thenReturn(List.of(expectedTransaction)); //remember this is a given not a when ARRANGE
        TransactionService service = new TransactionService(repository);
        //act
        List<Transaction> transactions = service.getTransactions();
        //then
        assertThat(transactions).containsExactly(expectedTransaction);
    }

}