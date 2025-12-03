package com.circulosiete.minibank.account.domain;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import org.jspecify.annotations.NullMarked;

@Embeddable
@Getter
@ToString
@NullMarked
public class Money {

    private String currency;
    private BigDecimal amount;

    protected Money() {
        // Requerido por JPA
    }

    public static Money mxn(BigDecimal amount) {
        return of("MXN", amount);
    }

    public static Money of(String currency, BigDecimal amount) {
        return new Money(currency, amount);
    }

    public Money(String currency, BigDecimal amount) {
        if (currency.isBlank()) {
            throw new IllegalArgumentException("currency cannot be blank");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount must be >= 0");
        }
        this.currency = currency;
        this.amount = amount;
    }

    public Money(String currency, String amount) {
        this(currency, new BigDecimal(amount));
    }

    public static Money zero(String currency) {
        return new Money(currency, BigDecimal.ZERO);
    }

    // --- Operaciones de dominio ---

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(currency, amount.add(other.amount));
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        if (amount.compareTo(other.amount) < 0) {
            throw new IllegalArgumentException("insufficient funds");
        }
        return new Money(currency, amount.subtract(other.amount));
    }

    private void requireSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                "currency mismatch: %s vs %s".formatted(currency, other.currency)
            );
        }
    }

    // Igualdad por valor
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Money money)) {
            return false;
        }
        return currency.equals(money.currency) &&
            amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, amount);
    }
}
