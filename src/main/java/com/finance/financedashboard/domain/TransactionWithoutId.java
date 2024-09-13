package com.finance.financedashboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionWithoutId {
    private String name;
    private long amount;
    private String description;
    private LocalDateTime date;
    private TransactionType type;


    public Transaction toNewTransaction() {

        return Transaction.builder().id(UUID.randomUUID().toString())
                .type(this.getType())
                .description(this.getDescription())
                .name(this.getName())
                .amount(this.getAmount())
                .date(this.getDate())
                .build();

    }
}
