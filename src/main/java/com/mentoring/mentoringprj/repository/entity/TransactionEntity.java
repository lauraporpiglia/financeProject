package com.mentoring.mentoringprj.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name="transaction")
public class TransactionEntity {

    @Id
    private String id;
    private String name;
    private int amount;
    private String description;
    private String type;
    private String date;

}
