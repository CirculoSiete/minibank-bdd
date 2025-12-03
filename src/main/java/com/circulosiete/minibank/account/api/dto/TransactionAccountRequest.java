package com.circulosiete.minibank.account.api.dto;

import java.math.BigDecimal;

public record TransactionAccountRequest(
    String currency,
    BigDecimal amount
) {
}
