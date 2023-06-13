package com.mentoring.mentoringprj.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("json")
public class JSONTransactionRepository implements TransactionRepository {
    private Path path;

    public JSONTransactionRepository(@Value("${transactions.json.path}") String path) {
        this.path = Paths.get(path);
    }

    @Override
    public List<Transaction> getTransactions() throws TransactionReadException {
        TransactionWrapper transactionWrapper;
        try {
            String fileContent = Files.readString(path);//never do in prod
            if (fileContent.isEmpty()) {
                return new ArrayList<>();
            }
            ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
            transactionWrapper = objectMapper.readValue(fileContent, TransactionWrapper.class);
        } catch (MismatchedInputException e) {
            throw new TransactionReadException("Data Type mismatch", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Transaction> transactions = transactionWrapper.getTransactions();
        validateTransaction(transactions);
        return transactions;


    }

    private void validateTransaction(List<Transaction> transactions) throws TransactionReadException {
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() <= 0) {
                throw new TransactionReadException("Incorrect amount");
            }
        }
    }
}
