package com.finance.financedashboard.repository.entity;

import com.finance.financedashboard.domain.Transaction;
import com.finance.financedashboard.domain.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "transaction")
public class TransactionEntity {

    @Id
    private String id;
    private String name;
    private long amount;
    private String description;
    private String type;
    private String date;


    public Transaction toTransaction() {
        Transaction.TransactionBuilder builder = Transaction.builder()
                .id(this.id)
                .name(this.name)
                .amount(this.amount)
                .description(this.description);


        if (this.type != null) {
            builder.type(TransactionType.valueOf(this.type));
        }

        if (this.date != null) {
            builder.date(LocalDateTime.parse(this.date));
        }

        return builder.build();
    }
    /*@todo:write test*/
}
