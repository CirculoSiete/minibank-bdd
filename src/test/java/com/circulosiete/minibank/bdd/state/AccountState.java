package com.circulosiete.minibank.bdd.state;

import codes.domix.fun.Result;
import com.circulosiete.minibank.account.api.AccountResponse;
import com.circulosiete.minibank.account.api.ApiError;
import com.circulosiete.minibank.account.api.client.AccountClient;
import io.cucumber.spring.ScenarioScope;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.UUID;

@Component
@ScenarioScope
public class AccountState {
    @LocalServerPort
    int port;
    private Result<AccountResponse, ApiError> accountResult;
    private UUID accountId;
    private AccountClient accountClient;

    public Result<AccountResponse, ApiError> getAccountResult() {
        return accountResult;
    }

    public void setAccountResult(Result<AccountResponse, ApiError> account) {
        this.accountResult = account;
    }

    public AccountClient getAccountClient() {
        if (accountClient == null) {
            accountClient = new AccountClient(
                RestClient.create("http://localhost:" + port)
            );
        }
        return accountClient;
    }

    public void setRestClient(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }
}
