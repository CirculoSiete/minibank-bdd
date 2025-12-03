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
    private final Error error = new Error();

    public Result<AccountResponse, ApiError> createAccount(CreateAccountRequest request) {
        try {
            return Result.ok(
                restClient
                    .post()
                    .uri("/v1/accounts")
                    .body(request)
                    .retrieve()
                    .body(AccountResponse.class)
            );
        } catch (Exception ex) {
            return error.getErrorResult(ex);
        }
    }

    public Result<AccountResponse, ApiError> getAccount(UUID accountId) {
        try {
            return Result.ok(
                restClient
                    .get()
                    .uri("/v1/accounts/%s".formatted(accountId.toString()))
                    .retrieve()
                    .body(AccountResponse.class)
            );
        } catch (Exception ex) {
            return error.getErrorResult(ex);
        }
    }

    public Result<AccountResponse, ApiError> deposit(UUID accountId, TransactionAccountRequest request) {
        try {
            final var uri = "/v1/accounts/%s/transactions"
                .formatted(accountId.toString());
            return Result.ok(
                restClient
                    .put()
                    .uri(uri)
                    .body(request)
                    .retrieve()
                    .body(AccountResponse.class)
            );
        } catch (Exception ex) {
            return error.getErrorResult(ex);
        }
    }

    public Result<AccountResponse, ApiError> withdraw(UUID accountId, TransactionAccountRequest request) {
        try {
            final var uri = "/v1/accounts/%s/transactions"
                .formatted(accountId.toString());
            return Result.ok(
                restClient
                    .method(HttpMethod.DELETE)
                    .uri(uri)
                    .body(request)
                    .retrieve()
                    .body(AccountResponse.class)
            );
        } catch (Exception ex) {
            return error.getErrorResult(ex);
        }
    }
}
