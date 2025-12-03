package com.circulosiete.minibank.account.api;

import java.math.BigDecimal;

public record TransactionAccountRequest(
    String currency,
    BigDecimal amount
) {
}
