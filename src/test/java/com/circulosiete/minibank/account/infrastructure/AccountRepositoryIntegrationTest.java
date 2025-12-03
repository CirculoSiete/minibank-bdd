package com.circulosiete.minibank.account.infrastructure;

//import com.example.account.domain.Account;
//import com.example.account.domain.AccountStatus;
//import com.example.account.domain.Money;

import com.circulosiete.minibank.TestcontainersConfiguration;
import com.circulosiete.minibank.account.domain.Money;
import com.circulosiete.minibank.account.domain.Account;
import com.circulosiete.minibank.account.domain.AccountStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    private Account newAccount(UUID ownerId, String currency, BigDecimal amount, AccountStatus status) {
        return new Account(
            UUID.randomUUID(),
            ownerId,
            status,
            new Money(currency, amount)
        );
    }

    @Test
    @DisplayName("Debe guardar y recuperar una Account por id")
    void shouldSaveAndFindById() {
        UUID ownerId = UUID.randomUUID();
        Account account = newAccount(ownerId, "USD", new BigDecimal("100.00"), AccountStatus.ACTIVE);

        Account saved = accountRepository.save(account);

        assertThat(saved.getId()).isNotNull();

        Account found = accountRepository.findById(saved.getId())
            .orElseThrow(() -> new AssertionError("Account not found"));

        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getOwnerId()).isEqualTo(ownerId);
        assertThat(found.getStatus()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(found.getBalance().getCurrency()).isEqualTo("USD");
        assertThat(found.getBalance().getAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("Debe encontrar cuentas por ownerId con paginación")
    void shouldFindByOwnerIdWithPagination() {
        UUID ownerId = UUID.randomUUID();
        UUID otherOwnerId = UUID.randomUUID();

        // 3 cuentas del mismo owner
        Account a1 = newAccount(ownerId, "USD", new BigDecimal("10.00"), AccountStatus.ACTIVE);
        Account a2 = newAccount(ownerId, "USD", new BigDecimal("20.00"), AccountStatus.BLOCKED);
        Account a3 = newAccount(ownerId, "USD", new BigDecimal("30.00"), AccountStatus.ACTIVE);

        // 1 cuenta de otro owner
        Account a4 = newAccount(otherOwnerId, "USD", new BigDecimal("40.00"), AccountStatus.ACTIVE);

        accountRepository.saveAll(List.of(a1, a2, a3, a4));

        Pageable pageRequest = PageRequest.of(0, 2); // primera página, size 2

        Page<Account> page = accountRepository.findByOwnerId(ownerId, pageRequest);

        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(2);

        // Segunda página
        Page<Account> secondPage = accountRepository.findByOwnerId(ownerId, PageRequest.of(1, 2));

        assertThat(secondPage.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("Debe filtrar por ownerId y status con paginación")
    void shouldFindByOwnerIdAndStatusWithPagination() {
        UUID ownerId = UUID.randomUUID();

        Account active1 = newAccount(ownerId, "USD", new BigDecimal("50.00"), AccountStatus.ACTIVE);
        Account active2 = newAccount(ownerId, "USD", new BigDecimal("60.00"), AccountStatus.ACTIVE);
        Account blocked = newAccount(ownerId, "USD", new BigDecimal("70.00"), AccountStatus.BLOCKED);

        accountRepository.saveAll(List.of(active1, active2, blocked));

        Pageable pageable = PageRequest.of(0, 10);

        Page<Account> activeAccounts = accountRepository
            .findByOwnerIdAndStatus(ownerId, AccountStatus.ACTIVE, pageable);

        assertThat(activeAccounts.getTotalElements()).isEqualTo(2);
        assertThat(activeAccounts.getContent())
            .extracting(Account::getStatus)
            .allMatch(status -> status == AccountStatus.ACTIVE);

        Page<Account> blockedAccounts = accountRepository
            .findByOwnerIdAndStatus(ownerId, AccountStatus.BLOCKED, pageable);

        assertThat(blockedAccounts.getTotalElements()).isEqualTo(1);
        assertThat(blockedAccounts.getContent().get(0).getStatus()).isEqualTo(AccountStatus.BLOCKED);
    }

    @Test
    @DisplayName("Debe mapear correctamente el embeddable Money (currency + amount)")
    void shouldPersistAndLoadMoneyEmbeddableCorrectly() {
        UUID ownerId = UUID.randomUUID();
        Money balance = new Money("MXN", new BigDecimal("999.99"));

        Account account = new Account(
            UUID.randomUUID(),
            ownerId,
            AccountStatus.ACTIVE,
            balance
        );

        accountRepository.save(account);

        Account found = accountRepository.findById(account.getId())
            .orElseThrow(() -> new AssertionError("Account not found"));

        assertThat(found.getBalance()).isNotNull();
        assertThat(found.getBalance().getCurrency()).isEqualTo("MXN");
        assertThat(found.getBalance().getAmount()).isEqualByComparingTo("999.99");
    }
}
