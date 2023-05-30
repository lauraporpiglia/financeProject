package com.mentoring.mentoringprj.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String name;
    private long amount;
    private String description;
    private LocalDateTime date;
    private TransactionType type;
}
