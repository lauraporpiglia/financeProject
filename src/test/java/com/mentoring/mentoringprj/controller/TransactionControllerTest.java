package com.mentoring.mentoringprj.controller;

import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class) //at every test it will remock the service
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService ;

    @Test
    void should_return_transactions() throws TransactionReadException {
        //given
        Transaction expectedTransaction =  Transaction.builder().build();
        when(transactionService.getTransactions()).thenReturn(List.of(expectedTransaction)); //we use when even if it's legacy
      // given(transactionService.getTransactions()).willReturn(List.of(expectedTransaction)); // do not use
        TransactionController transactionController = new TransactionController(transactionService);
        //when
        List<Transaction> transactions = transactionController.getTransactions();
        //then
        assertThat(transactions).containsExactly(expectedTransaction);

    }


}