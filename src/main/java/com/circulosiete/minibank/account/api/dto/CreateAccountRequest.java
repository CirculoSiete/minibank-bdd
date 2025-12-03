package com.circulosiete.minibank.account.api.dto;

import java.util.UUID;

public record CreateAccountRequest(
    UUID ownerId,
    String currency
) {
}
