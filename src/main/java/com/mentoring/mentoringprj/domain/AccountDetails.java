package com.mentoring.mentoringprj.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class AccountDetails {
    List<Transaction> transactions;
    long balance;
}
