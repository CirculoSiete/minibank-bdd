package com.circulosiete.minibank.account.application;

import com.circulosiete.minibank.account.domain.Money;
import com.circulosiete.minibank.account.infrastructure.AccountRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@NullMarked
@RequiredArgsConstructor
public class TransferService {
    private final AccountRepository accountRepository;

    @Transactional
    public void transfer(UUID fromAccountId, UUID toAccountId, Money amount) {
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("source and destination accounts cannot be the same");
        }

        final var from = accountRepository.findById(fromAccountId)
            .orElseThrow(() -> new IllegalArgumentException("source account not found"));

        final var to = accountRepository.findById(toAccountId)
            .orElseThrow(() -> new IllegalArgumentException("destination account not found"));

        // Validación de moneda igual
        if (!from.getBalance().getCurrency().equals(to.getBalance().getCurrency())) {
            throw new IllegalArgumentException("currency mismatch between accounts");
        }

        // Operaciones de dominio
        from.withdraw(amount);
        to.deposit(amount);

        // Persistimos ambos
        accountRepository.save(from);
        accountRepository.save(to);
    }
}
