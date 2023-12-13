package com.mentoring.mentoringprj.repository;

import com.mentoring.mentoringprj.repository.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
}
