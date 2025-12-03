package com.circulosiete.minibank.account.api;

import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record TransferResponse(
    UUID fromAccountId,
    UUID toAccountId,
    String status
) {
}
