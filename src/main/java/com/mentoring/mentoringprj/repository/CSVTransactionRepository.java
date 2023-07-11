package com.mentoring.mentoringprj.repository;

import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.domain.TransactionType;
import com.mentoring.mentoringprj.exceptions.AmountException;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Qualifier("csv")
@Repository
public class CSVTransactionRepository implements TransactionRepository {
    private final String path;
    private final CSVParser parser;

    //do not use autowired @Value
    public CSVTransactionRepository(@Value("${transactions.path}") String path) {
        this.path = path;
        this.parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();
    }

    @Override
    public List<Transaction> getTransactions() throws TransactionReadException {

        List<String[]> records;
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(path))
                .withSkipLines(1)
                .withCSVParser(parser)
                .build()) {
            records = csvReader.readAll();

            return createTransactionsFromRecords(records);
        } catch (CsvException | IOException e) {
            throw new RuntimeException(e);

        } catch (NumberFormatException e) {
            throw new TransactionReadException("could not parse number in the transaction", e);
        } catch (IllegalArgumentException e) {
            throw new TransactionReadException("could not parse transaction type", e);
        }

    }

    @Override
    public void addTransaction(Transaction transaction) {
        throw new UnsupportedOperationException("Can't add a transaction using CSV repository");
    }

    @Override
    public void deleteTransaction(String id) throws TransactionReadException, IOException {
       throw new UnsupportedOperationException("Can't delete transaction using CSV repository");
    }

    private List<Transaction> createTransactionsFromRecords(List<String[]> records) throws TransactionReadException {
        List<Transaction> list = new ArrayList<>();
        for (String[] record : records) {
            Transaction apply = apply(record);
            list.add(apply);
        }
        return list;
    }

    private String checkAmount(String amount) throws AmountException {

        try {
            if (Long.parseLong(amount) <= 0) {
                throw new AmountException("Transaction amount incorrect should be greater than 0");
            }
        } catch (NumberFormatException nfe) {
            throw new AmountException("Amount is not a number", nfe);
        }
        return amount;
    }

    private Transaction apply(String[] record) throws TransactionReadException {
        try {
            return Transaction.builder()
                    .name(record[0])
                    .amount(Long.parseLong(checkAmount(record[1])))
                    .description(record[2])
                    .date(LocalDateTime.parse(record[3]))
                    .type(TransactionType.valueOf(record[4]))
                    .build();
        } catch (IllegalArgumentException e) {
            throw new TransactionReadException("could not parse transaction type", e);
        } catch (AmountException e) {
            throw new TransactionReadException("Incorrect amount", e);
        } catch (Exception e) {
            throw new TransactionReadException(" ", e);
        }
    }
}
