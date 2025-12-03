package com.circulosiete.minibank.account.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jspecify.annotations.NullMarked;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@NullMarked
@Entity
@Table(name = "account")
public class Account {
    @Id
    @ToString.Include
    private UUID id;
    @ToString.Include
    private UUID ownerId;
    @ToString.Include
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @ToString.Include
    @Embedded
    private Money balance;

    // --- Métodos de dominio ---

    public void deposit(Money amount) {
        requireActive();
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        requireActive();
        try {
            this.balance = this.balance.subtract(amount);
        } catch (IllegalArgumentException e) {
            // Reproyectar a excepción de dominio propia
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }

    public void block() {
        this.status = AccountStatus.BLOCKED;
    }

    public void close() {
        this.status = AccountStatus.CLOSED;
    }

    private void requireActive() {
        if (this.status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account must be ACTIVE");
        }
    }
}
