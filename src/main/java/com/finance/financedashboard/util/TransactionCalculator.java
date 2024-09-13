package com.finance.financedashboard.util;

import com.finance.financedashboard.domain.Transaction;
import com.finance.financedashboard.domain.TransactionType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionCalculator {
  public long calculateTotal(List<Transaction> transactions) {
      long total=0;

        for (Transaction transaction : transactions) {
            if(TransactionType.CREDIT.equals(transaction.getType())) {
                total += transaction.getAmount();
            }else{
                total -= transaction.getAmount();
            }
        }
        return total;
    }
}
