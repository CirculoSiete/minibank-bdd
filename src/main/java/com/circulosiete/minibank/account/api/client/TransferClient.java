package com.circulosiete.minibank.account.api.client;

import codes.domix.fun.Result;
import com.circulosiete.minibank.account.api.dto.ApiError;
import com.circulosiete.minibank.account.api.dto.TransferRequest;
import com.circulosiete.minibank.account.api.dto.TransferResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class TransferClient {
    private final RestClient restClient;
    private final HttpClientUtil httpClientUtil = new HttpClientUtil();

    public Result<TransferResponse, ApiError> transfer(TransferRequest request) {
        return httpClientUtil.invokes(
            () ->
                restClient
                    .post()
                    .uri("/v1/transfers")
                    .body(request)
                    .retrieve()
                    .body(TransferResponse.class)

        );
    }
}
