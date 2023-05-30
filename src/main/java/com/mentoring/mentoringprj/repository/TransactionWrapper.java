package com.mentoring.mentoringprj.repository;

import com.mentoring.mentoringprj.domain.Transaction;
import lombok.Data;

import java.util.List;
@Data
public class TransactionWrapper {
    private List<Transaction> transactions;

}
