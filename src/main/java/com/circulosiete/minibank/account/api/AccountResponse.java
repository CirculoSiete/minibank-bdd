package com.circulosiete.minibank.account.api;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(
    UUID id,
    UUID ownerId,
    String status,
    String currency,
    BigDecimal balance
) {
}
