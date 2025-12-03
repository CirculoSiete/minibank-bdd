package com.circulosiete.minibank.account.api.controller;

import com.circulosiete.minibank.account.api.dto.AccountMapper;
import com.circulosiete.minibank.account.api.dto.AccountResponse;
import com.circulosiete.minibank.account.api.dto.CreateAccountRequest;
import com.circulosiete.minibank.account.api.dto.TransactionAccountRequest;
import com.circulosiete.minibank.account.application.AccountService;
import com.circulosiete.minibank.account.domain.Money;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;

    @PostMapping
    public ResponseEntity<AccountResponse> create(
        @RequestBody CreateAccountRequest request
    ) {
        final var created = service.createAccount(request);
        return ResponseEntity.ok(
            AccountMapper.toResponse(created)
        );
    }

    @GetMapping(value = "/{accountId}")
    public ResponseEntity<AccountResponse> get(
        @PathVariable("accountId") UUID accountId
    ) {
        final var account = service.getAccount(accountId);
        return ResponseEntity.ok(
            AccountMapper.toResponse(account)
        );
    }

    @PutMapping(value = "/{accountId}/transactions")
    public ResponseEntity<AccountResponse> deposit(
        @PathVariable("accountId") UUID accountId,
        @RequestBody TransactionAccountRequest request
    ) {
        final var account = service.deposit(
            accountId,
            Money.of(request.currency(), request.amount())
        );
        return ResponseEntity.ok(
            AccountMapper.toResponse(account)
        );
    }

    @DeleteMapping(value = "/{accountId}/transactions")
    public ResponseEntity<AccountResponse> withdraw(
        @PathVariable("accountId") UUID accountId,
        @RequestBody TransactionAccountRequest request
    ) {
        final var account = service.withdraw(
            accountId,
            Money.of(request.currency(), request.amount())
        );
        return ResponseEntity.ok(
            AccountMapper.toResponse(account)
        );
    }
}
