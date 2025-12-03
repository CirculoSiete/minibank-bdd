package com.circulosiete.minibank.account.api.client;

import codes.domix.fun.Result;
import com.circulosiete.minibank.account.api.dto.AccountResponse;
import com.circulosiete.minibank.account.api.dto.ApiError;
import com.circulosiete.minibank.account.api.dto.TransferResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.web.client.HttpClientErrorException;

public class Error {
    private final ObjectMapper objectMapper;

    public Error() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @SneakyThrows
    public Result<AccountResponse, ApiError> getErrorResult(Exception ex) {
        if (ex instanceof HttpClientErrorException httpEx && httpEx.getStatusCode().is4xxClientError()) {
            return Result.err(
                objectMapper.readValue(
                    httpEx.getResponseBodyAsString(),
                    ApiError.class
                )
            );
        }
        throw new RuntimeException(ex);
    }

    @SneakyThrows
    public Result<TransferResponse, ApiError> getTransferError(Throwable ex) {
        if (ex instanceof HttpClientErrorException httpEx && httpEx.getStatusCode().is4xxClientError()) {
            return Result.err(
                objectMapper.readValue(
                    httpEx.getResponseBodyAsString(),
                    ApiError.class
                )
            );
        }
        throw new RuntimeException(ex);
    }
}
