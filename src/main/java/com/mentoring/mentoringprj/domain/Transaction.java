package com.mentoring.mentoringprj.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transaction {
    private final String name;
    private final long amount;
}
