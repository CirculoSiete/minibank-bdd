package com.circulosiete.minibank.account.api.client;

import codes.domix.fun.Result;
import com.circulosiete.minibank.account.api.ApiError;
import com.circulosiete.minibank.account.api.TransferRequest;
import com.circulosiete.minibank.account.api.TransferResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class TransferClient {
    private final RestClient restClient;
    private final Error error = new Error();

    public Result<TransferResponse, ApiError> transfer(TransferRequest request) {
        try {
            return Result.ok(
                restClient
                    .post()
                    .uri("/v1/transfers")
                    .body(request)
                    .retrieve()
                    .body(TransferResponse.class)
            );
        } catch (Throwable ex) {
            return error.getTransferError(ex);
        }
    }
}
