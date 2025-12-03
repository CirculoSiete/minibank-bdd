package com.circulosiete.minibank.account.domain;

import com.fasterxml.uuid.Generators;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class AccountFactory {

    public Account openAccount(UUID ownerId, String currency) {
        final var accountId = Generators.timeBasedGenerator().generate();
        final var balance = Money.zero(currency);
        return new Account(accountId, ownerId, AccountStatus.ACTIVE, balance);
    }

    public Account openAccountWithInitialBalance(UUID ownerId, Money initialBalance) {
        final var accountId = Generators.timeBasedGenerator().generate();
        return new Account(accountId, ownerId, AccountStatus.ACTIVE, initialBalance);
    }
}
