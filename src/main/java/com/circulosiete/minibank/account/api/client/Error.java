package com.circulosiete.minibank.account.api.client;

import codes.domix.fun.Try;
import com.circulosiete.minibank.account.api.dto.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
public class Error {
    private final ObjectMapper objectMapper;

    public Error() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public ApiError getApiError(Throwable ex) {
        if (ex instanceof HttpClientErrorException httpEx && httpEx.getStatusCode().is4xxClientError()) {
            return getApiError(httpEx.getResponseBodyAsString());
        }
        log.error("Unknow error", ex);
        return new ApiError(
            "UNKNOW", "Unknow error", ex.getMessage(), OffsetDateTime.now()
        );
    }

    public ApiError getApiError(String body) {
        return Try
            .of(() -> objectMapper.readValue(body, ApiError.class))
            .onFailure(ex -> log.error("Error parsing ApiError from body", ex))
            .getOrElseGet(
                () -> new ApiError(
                    "UNKNOW",
                    "Unknow error",
                    body,
                    OffsetDateTime.now()
                )
            );
    }
}
