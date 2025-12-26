package com.circulosiete.minibank.account.api.client;

import codes.domix.fun.Result;
import com.circulosiete.minibank.account.api.dto.AccountResponse;
import com.circulosiete.minibank.account.api.dto.ApiError;
import com.circulosiete.minibank.account.api.dto.CreateAccountRequest;
import com.circulosiete.minibank.account.api.dto.TransactionAccountRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class AccountClient {
    private final RestClient restClient;
    private final HttpClientUtil httpClientUtil = new HttpClientUtil();

    public Result<AccountResponse, ApiError> createAccount(CreateAccountRequest request) {
        return httpClientUtil.invokes(
            () -> restClient
                .post()
                .uri("/v1/accounts")
                .body(request)
                .retrieve()
                .body(AccountResponse.class)
        );
    }

    public Result<AccountResponse, ApiError> getAccount(UUID accountId) {
        return httpClientUtil.invokes(
            () -> restClient
                .get()
                .uri("/v1/accounts/%s".formatted(accountId.toString()))
                .retrieve()
                .body(AccountResponse.class)
        );
    }

    public Result<AccountResponse, ApiError> deposit(UUID accountId, TransactionAccountRequest request) {
        return httpClientUtil.invokes(
            () -> {
                final var uri = "/v1/accounts/%s/transactions"
                    .formatted(accountId.toString());
                return restClient
                    .put()
                    .uri(uri)
                    .body(request)
                    .retrieve()
                    .body(AccountResponse.class);
            }
        );
    }

    public Result<AccountResponse, ApiError> withdraw(UUID accountId, TransactionAccountRequest request) {
        return httpClientUtil.invokes(
            () -> {
                final var uri = "/v1/accounts/%s/transactions"
                    .formatted(accountId.toString());
                return restClient
                    .method(HttpMethod.DELETE)
                    .uri(uri)
                    .body(request)
                    .retrieve()
                    .body(AccountResponse.class);
            }
        );
    }
}
