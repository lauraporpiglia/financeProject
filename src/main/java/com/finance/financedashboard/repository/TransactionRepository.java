package com.finance.financedashboard.repository;

import com.finance.financedashboard.repository.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
}
