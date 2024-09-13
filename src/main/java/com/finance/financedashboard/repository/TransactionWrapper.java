package com.finance.financedashboard.repository;

import com.finance.financedashboard.domain.Transaction;
import lombok.Data;

import java.util.List;
@Data
public class TransactionWrapper {
    private List<Transaction> transactions;

}
