package com.circulosiete.minibank.account.application;

import com.circulosiete.minibank.TestcontainersConfiguration;
import com.circulosiete.minibank.account.domain.Money;
import com.circulosiete.minibank.account.infrastructure.AccountRepository;
import com.fasterxml.uuid.Generators;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
class TransferServiceTest {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private TransferService transferService;
    @Autowired
    private AccountService accountService;

    @Test
    void transferBetweenAccountsWorks() {
        // Arrange
        final var owner1 = Generators.timeBasedGenerator().generate();
        final var owner2 = Generators.timeBasedGenerator().generate();

        final var account1 = accountService.openAccount(
            owner1,
            "MXN"
        );
        accountService.deposit(
            account1.getId(), Money.mxn(new BigDecimal("1000"))
        );
        final var account2 = accountService.openAccount(
            owner2,
            "MXN"
        );
        accountService.deposit(
            account2.getId(), Money.mxn(new BigDecimal("500"))
        );

        // Act
        transferService.transfer(
            account1.getId(),
            account2.getId(),
            Money.mxn(new BigDecimal("200"))
        );

        // Assert
        final var updatedA = repository.findById(account1.getId()).get();
        final var updatedB = repository.findById(account2.getId()).get();

        assertThat(updatedA.getBalance().getAmount())
            .isEqualByComparingTo("800");
        assertThat(updatedB.getBalance().getAmount())
            .isEqualByComparingTo("700");
    }
}
