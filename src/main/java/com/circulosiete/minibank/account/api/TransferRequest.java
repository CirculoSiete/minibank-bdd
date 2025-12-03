package com.circulosiete.minibank.account.api;

import java.math.BigDecimal;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TransferRequest(
    UUID fromAccountId,
    UUID toAccountId,
    String currency,
    BigDecimal amount
) {
}
