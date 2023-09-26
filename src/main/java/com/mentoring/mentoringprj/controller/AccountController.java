package com.mentoring.mentoringprj.controller;

import com.mentoring.mentoringprj.domain.AccountDetails;
import com.mentoring.mentoringprj.domain.Transaction;
import com.mentoring.mentoringprj.domain.TransactionWithoutId;
import com.mentoring.mentoringprj.exceptions.TransactionReadException;
import com.mentoring.mentoringprj.service.AccountService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService transactionService) {
        this.accountService = transactionService;
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountDetails getAccountDetails(
            @RequestParam(name = "from", required = false) Optional<LocalDateTime> from,
            @RequestParam(name = "to", required = false) Optional<LocalDateTime> to) throws TransactionReadException {

        return accountService.getAccountDetails(from, to);
    }

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountDetails addTransaction(@RequestBody Transaction transaction) throws TransactionReadException, IOException {
        return accountService.addTransaction(transaction);
    }

    @DeleteMapping(path = "{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountDetails deleteTransaction(@PathVariable("transactionId") String transactionId) throws TransactionReadException, IOException {
        return accountService.delete(transactionId);
    }

   //@todo pathvariable transactionid is already in the requestbody
   @PutMapping(path = "/update/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountDetails updateTransaction(@PathVariable("transactionId") String transactionId, @RequestBody TransactionWithoutId transaction) throws TransactionReadException, IOException {
        return accountService.updateTransaction(transactionId,transaction);
    }
}
