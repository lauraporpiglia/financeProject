package com.mentoring.mentoringprj.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.exceptions.TransactionNotFoundException;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
@Qualifier("json")
public class JSONTransactionRepository {
    private final Path path;
    private final ObjectMapper objectMapper;

    public JSONTransactionRepository(@Value("${transactions.json.path}") String path, ObjectMapper objectMapper) {
        this.path = Paths.get(path);
        this.objectMapper = objectMapper;
    }

    public List<Transaction> getTransactions() throws TransactionReadException {
        TransactionWrapper transactionWrapper;
        try {
            String fileContent = Files.readString(path);//never do in prod
            if (fileContent.isEmpty()) {
                return new ArrayList<>();
            }

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


    public void addTransaction(Transaction transaction) throws TransactionReadException, IOException {
        List<Transaction> transactions = getTransactions();
        transactions.add(transaction);

        TransactionWrapper transWrapper = new TransactionWrapper();
        transWrapper.setTransactions(transactions);

        byte[] bytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(transWrapper);


        try (OutputStream outStream = new FileOutputStream(path.toFile())) {
            outStream.write(bytes);

            IOUtils.closeQuietly(outStream);
        }
    }

    public void deleteTransaction(String id) throws TransactionReadException, IOException, TransactionNotFoundException {
        if (getTransactionsById(id).isEmpty()) {
            throw new TransactionNotFoundException("Transaction not found with id %s".formatted(id));
        }
        List<Transaction> transactions = getTransactions();
        List<Transaction> filteredTransactions = transactions.stream().filter(transaction -> !transaction.getId().equals(id)).toList();

        if (filteredTransactions.isEmpty()) {
            throw new TransactionReadException("Transaction not found");
        }
        TransactionWrapper transWrapper = new TransactionWrapper();
        transWrapper.setTransactions(filteredTransactions);

        byte[] bytes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(transWrapper);

        OutputStream outStream = new FileOutputStream(path.toFile());
        outStream.write(bytes);

        IOUtils.closeQuietly(outStream);
    }

    public void updateTransaction(Transaction transaction) throws TransactionReadException, IOException, TransactionNotFoundException {
        deleteTransaction(transaction.getId());
        addTransaction(transaction);
    }


    private void validateTransaction(List<Transaction> transactions) throws TransactionReadException {
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() <= 0) {
                throw new TransactionReadException("Incorrect amount");
            }
        }
    }

    private Optional<Transaction> getTransactionsById(String transactionId) throws TransactionReadException {
        return getTransactions().stream()
                .filter(t -> t.getId().equals(transactionId))
                .findFirst();
    }
}
