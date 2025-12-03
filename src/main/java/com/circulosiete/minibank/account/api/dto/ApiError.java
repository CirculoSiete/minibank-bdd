package com.circulosiete.minibank.account.api.dto;

import java.time.OffsetDateTime;

public record ApiError(
    String type,          // URI o identificador de tu catálogo de errores
    String title,         // Mensaje corto para UI
    String detail,        // Detalle técnico
    OffsetDateTime timestamp
) {
    public static ApiError of(String type, String title, String detail) {
        return new ApiError(type, title, detail, OffsetDateTime.now());
    }
}
