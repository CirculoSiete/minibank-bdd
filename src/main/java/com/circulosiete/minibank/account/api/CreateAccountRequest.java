package com.circulosiete.minibank.account.api;

import java.util.UUID;

public record CreateAccountRequest(
    UUID ownerId,
    String currency
) {
}
