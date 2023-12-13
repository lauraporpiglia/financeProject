package com.mentoring.mentoringprj.domain;

import com.mentoring.mentoringprj.repository.entity.TransactionEntity;
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
    private String id;
    private String name;
    private long amount;
    private String description;
    private LocalDateTime date;
    private TransactionType type;

    public TransactionEntity toTransactionEntity() {

        TransactionEntity.TransactionEntityBuilder builder = TransactionEntity.builder()
                .id(this.id)
                .name(this.name)
                .amount(this.amount)
                .description(this.description);

        if (this.date != null) {
            builder.date(String.valueOf(this.date));
        }
        if (this.type != null) {
            builder.type(String.valueOf(this.type));
        }


        return builder.build();
    }
}
