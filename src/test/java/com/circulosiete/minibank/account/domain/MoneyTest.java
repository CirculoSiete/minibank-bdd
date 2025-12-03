package com.circulosiete.minibank.account.domain;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    @Test
    void createMoneyWithValidValues() {
        Money money = new Money("USD", new BigDecimal("100.00"));
        assertEquals("USD", money.getCurrency());
        assertEquals(new BigDecimal("100.00"), money.getAmount());
    }

    @Test
    void zeroFactoryCreatesZeroAmount() {
        Money money = Money.zero("MXN");
        assertEquals("MXN", money.getCurrency());
        assertEquals(BigDecimal.ZERO, money.getAmount());
    }

    @Test
    void addRequiresSameCurrency() {
        Money m1 = new Money("USD", new BigDecimal("10"));
        Money m2 = new Money("USD", new BigDecimal("5"));

        Money result = m1.add(m2);

        assertEquals(new BigDecimal("15"), result.getAmount());
        assertEquals("USD", result.getCurrency());
    }

    @Test
    void addWithDifferentCurrencyThrows() {
        Money usd = new Money("USD", new BigDecimal("10"));
        Money mxn = new Money("MXN", new BigDecimal("5"));

        assertThrows(IllegalArgumentException.class, () -> usd.add(mxn));
    }

    @Test
    void subtractWithInsufficientFundsThrows() {
        Money m1 = new Money("USD", new BigDecimal("10"));
        Money m2 = new Money("USD", new BigDecimal("20"));

        assertThrows(IllegalArgumentException.class, () -> m1.subtract(m2));
    }

    @Test
    void negativeAmountNotAllowed() {
        assertThrows(IllegalArgumentException.class,
            () -> new Money("USD", new BigDecimal("-1")));
    }
}
