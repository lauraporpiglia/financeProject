package com.mentoring.mentoringprj.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Transaction {
    private final String name;
    private final long amount;
    private final String description;
    private final LocalDateTime date;
    private final TransactionType type;
}
