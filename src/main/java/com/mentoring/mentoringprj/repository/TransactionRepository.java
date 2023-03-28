package com.mentoring.mentoringprj.repository;

import com.mentoring.mentoringprj.domain.Transaction;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TransactionRepository {
    private final CSVReader csvReader;

    //do not use autowired @Value
    public TransactionRepository(@Value("${transactions.path}") String path) {
        try {
            Reader fileReader = new FileReader(path);
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(',')
                    .withIgnoreQuotations(true)
                    .build();

            csvReader = new CSVReaderBuilder(fileReader)
                    .withSkipLines(1)
                    .withCSVParser(parser)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Transaction> getTransactions() {

        List<String[]> records;
        try {
            records = csvReader.readAll();
        } catch (CsvException | IOException e) {
            throw new RuntimeException(e);
        }
        return records.stream().map(record -> Transaction.builder()
                        .name(record[0])
                        .amount(Long.parseLong(record[1]))
                        .description(record[2])
                        .date(LocalDateTime.parse(record[3]))
                        .type(record[4])
                        .build())
                .collect(Collectors.toList());

    }
}
