package com.mentoring.mentoringprj.service;

import com.mentoring.mentoringprj.domain.AccountDetails;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.domain.TransactionType;
import com.mentoring.mentoringprj.exceptions.AmountException;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.repository.TransactionRepository;
import com.mentoring.mentoringprj.util.TransactionCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private TransactionRepository repository;

    @Test
    void should_return_transactions() throws TransactionReadException, AmountException {
        Transaction expectedTransaction =  Transaction.builder().type(TransactionType.CREDIT).amount(300).build();
        when(repository.getTransactions()).thenReturn(List.of(expectedTransaction)); //remember this is a given not a when ARRANGE
        AccountService service = new AccountService(repository, new TransactionCalculator());
        //act
        AccountDetails accountDetails = service.getAccountDetails();
        //then
        assertThat(accountDetails.getTransactions()).containsExactly(expectedTransaction);
        assertThat(accountDetails.getBalance()).isEqualTo(300);
    }
}
