package com.circulosiete.minibank.account.application;

import com.circulosiete.minibank.account.api.CreateAccountRequest;
import com.circulosiete.minibank.account.domain.Account;
import com.circulosiete.minibank.account.domain.AccountFactory;
import com.circulosiete.minibank.account.domain.Money;
import com.circulosiete.minibank.account.infrastructure.AccountRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.accountFactory = new AccountFactory();
    }

    public Account openAccount(UUID ownerId, String currency) {
        Account account = accountFactory.openAccount(ownerId, currency);
        return accountRepository.save(account);
    }

    public Account getAccount(UUID accountId) {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
    }

    public Page<Account> listAccountsByOwner(UUID ownerId, Pageable pageable) {
        return accountRepository.findByOwnerId(ownerId, pageable);
    }

    public Account deposit(UUID accountId, Money amount) {
        Account account = getAccount(accountId);
        account.deposit(amount);
        return accountRepository.save(account);
    }

    public Account withdraw(UUID accountId, Money amount) {
        Account account = getAccount(accountId);
        account.withdraw(amount);
        return accountRepository.save(account);
    }

    public Account blockAccount(UUID accountId) {
        Account account = getAccount(accountId);
        account.block();
        return accountRepository.save(account);
    }

    public Account closeAccount(UUID accountId) {
        Account account = getAccount(accountId);
        account.close();
        return accountRepository.save(account);
    }

    @Transactional
    public Account createAccount(CreateAccountRequest request) {
        final var account = openAccount(
            request.ownerId(),
            request.currency()
        );

        return accountRepository.save(account);
    }
}
