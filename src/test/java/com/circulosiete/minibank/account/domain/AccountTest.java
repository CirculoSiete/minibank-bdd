package com.circulosiete.minibank.account.domain;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

import static com.fasterxml.uuid.Generators.timeBasedGenerator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    private Account newActiveAccount(BigDecimal initialAmount) {
        final var ownerId = timeBasedGenerator().generate();
        final var balance = new Money("USD", initialAmount);
        return new Account(timeBasedGenerator().generate(), ownerId, AccountStatus.ACTIVE, balance);
    }

    @Test
    void createAccountWithValidData() {
        final var ownerId = timeBasedGenerator().generate();
        final var balance = new Money("USD", new BigDecimal("100.00"));

        final var account = new Account(
            timeBasedGenerator().generate(),
            ownerId,
            AccountStatus.ACTIVE,
            balance
        );

        assertEquals(ownerId, account.getOwnerId());
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
        assertEquals(new BigDecimal("100.00"), account.getBalance().getAmount());
        assertEquals("USD", account.getBalance().getCurrency());
    }

    @Test
    void depositIncreasesBalance() {
        final var account = newActiveAccount(new BigDecimal("100.00"));

        account.deposit(new Money("USD", new BigDecimal("50.00")));

        assertEquals(new BigDecimal("150.00"), account.getBalance().getAmount());
    }

    @Test
    void withdrawDecreasesBalance() {
        final var account = newActiveAccount(new BigDecimal("100.00"));

        account.withdraw(new Money("USD", new BigDecimal("40.00")));

        assertEquals(new BigDecimal("60.00"), account.getBalance().getAmount());
    }

    @Test
    void withdrawWithInsufficientBalanceThrowsDomainException() {
        final var account = newActiveAccount(new BigDecimal("50.00"));

        assertThrows(InsufficientBalanceException.class,
            () -> account.withdraw(new Money("USD", new BigDecimal("100.00"))));
    }

    @Test
    void operationsOnNonActiveAccountFail() {
        Account account = newActiveAccount(new BigDecimal("100.00"));
        account.block();

        assertThrows(IllegalStateException.class,
            () -> account.deposit(new Money("USD", new BigDecimal("10.00"))));

        assertThrows(IllegalStateException.class,
            () -> account.withdraw(new Money("USD", new BigDecimal("10.00"))));
    }

    @Test
    void closeChangesStatusToClosed() {
        Account account = newActiveAccount(new BigDecimal("0.00"));

        account.close();

        assertEquals(AccountStatus.CLOSED, account.getStatus());
    }
}
