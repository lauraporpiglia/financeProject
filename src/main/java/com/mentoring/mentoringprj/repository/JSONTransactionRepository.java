package com.mentoring.mentoringprj.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Repository

public class JSONTransactionRepository implements TransactionRepository {
    private Path path;

    public JSONTransactionRepository(@Value("${transactions.json.path}") String path) {
        this.path = Paths.get(path);
    }

    @Override
    public List<Transaction> getTransactions() throws TransactionReadException {
        try {
            String fileContent = Files.readString(path);//never do in prod
            ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
            TransactionWrapper transactionWrapper = objectMapper.readValue(fileContent, TransactionWrapper.class);
            return transactionWrapper.getTransactions();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
