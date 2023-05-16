package com.mentoring.mentoringprj.controller;

import com.mentoring.mentoringprj.domain.AccountDetails;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.domain.TransactionType;
import com.mentoring.mentoringprj.exceptions.AmountException;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //at every test it will remock the service
class TransactionControllerTest {

    @Mock
    private AccountService transactionService ;

    @Test
    void should_return_transactions() throws TransactionReadException, AmountException {
        //given
        Transaction expectedTransaction =  Transaction.builder().amount(1111).type(TransactionType.CREDIT).build();
        List<Transaction> expectedTransactions = List.of(expectedTransaction);
        AccountDetails accountDetails = AccountDetails.builder().transactions(expectedTransactions).build();
        when(transactionService.getAccountDetails()).thenReturn(accountDetails); //we use when even if it's legacy
      // given(transactionService.getTransactions()).willReturn(List.of(expectedTransaction)); // do not use
        AccountController accountController = new AccountController(transactionService);
        //when
    //   AccountDetails result = accountController.getAccountDetails();
        //then
      //  assertThat(result).isEqualTo(accountDetails);

    }


}