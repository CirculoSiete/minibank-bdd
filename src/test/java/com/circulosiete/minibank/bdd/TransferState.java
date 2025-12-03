package com.circulosiete.minibank.bdd;

import codes.domix.fun.Result;
import com.circulosiete.minibank.account.api.AccountResponse;
import com.circulosiete.minibank.account.api.ApiError;
import com.circulosiete.minibank.account.api.TransferResponse;
import com.circulosiete.minibank.account.api.client.AccountClient;
import com.circulosiete.minibank.account.api.client.TransferClient;
import io.cucumber.spring.ScenarioScope;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@ScenarioScope
public class TransferState {
    @LocalServerPort
    int port;
    private Result<TransferResponse, ApiError> transferResult;
    private AccountClient accountClient;
    private TransferClient transferClient;
    private Map<String, Result<AccountResponse, ApiError>> accounts = new HashMap<>();

    public Optional<AccountResponse> findAccount(String accountId) {
        return Optional.ofNullable(accounts.get(accountId))
            .flatMap(result -> Optional.ofNullable(result.getOrNull()));
    }

    public AccountResponse getAccount(String accountId) {
        return findAccount(accountId).orElseThrow();
    }

    public AccountClient getAccountClient() {
        if (accountClient == null) {
            accountClient = new AccountClient(
                RestClient.create("http://localhost:" + port)
            );
        }
        return accountClient;
    }

    public Result<TransferResponse, ApiError> getTransferResult() {
        return transferResult;
    }

    public void setTransferResult(Result<TransferResponse, ApiError> transferResult) {
        this.transferResult = transferResult;
    }

    public TransferClient getTransferClient() {
        if (transferClient == null) {
            transferClient = new TransferClient(
                RestClient.create("http://localhost:" + port)
            );
        }
        return transferClient;
    }

    public void setTransferClient(TransferClient transferClient) {
        this.transferClient = transferClient;
    }

    public Map<String, Result<AccountResponse, ApiError>> getAccounts() {
        return accounts;
    }

    public void setAccounts(Map<String, Result<AccountResponse, ApiError>> accounts) {
        this.accounts = accounts;
    }
}
