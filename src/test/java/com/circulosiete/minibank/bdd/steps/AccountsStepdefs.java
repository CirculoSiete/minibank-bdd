package com.circulosiete.minibank.bdd.steps;

import com.circulosiete.minibank.account.api.CreateAccountRequest;
import com.circulosiete.minibank.account.api.TransactionAccountRequest;
import com.circulosiete.minibank.bdd.state.AccountState;
import com.fasterxml.uuid.Generators;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


public class AccountsStepdefs {
    @Autowired
    private AccountState accountState;

    @When("creo una cuenta nueva para {string}")
    public void creoUnaCuentaNueva(String currency) {
        final var createdAccount = accountState
            .getAccountClient()
            .createAccount(
                new CreateAccountRequest(
                    Generators.timeBasedEpochGenerator().generate(),
                    currency
                )
            );

        this.accountState.setAccountResult(createdAccount);
        this.accountState.setAccountId(createdAccount.get().id());
    }

    @Then("la cuenta debe tener balance de {string} {string}")
    public void laCuentaCreadaDebeSerValidada(String amount, String currency) {
        final var createdAccountResult = accountState.getAccountResult();
        assertThat(createdAccountResult.isOk())
            .isTrue();

        final var createdAccount = createdAccountResult.get();

        assertThat(createdAccount)
            .as("Validar que la cuenta se creo con los datos esperados.")
            .isNotNull();

        assertThat(createdAccount.balance())
            .as("Validar que la cuenta tiene un balance inicial esperado.")
            .isEqualByComparingTo(new BigDecimal(amount));

        assertThat(createdAccount.currency())
            .as("Validar que la moneda de la cuenta es la esperada.")
            .isEqualTo(currency);
    }

    @Then("deposito a la cuenta {string} {string}")
    public void depositoALaCuenta(String amount, String currency) {
        final var accountResult = accountState
            .getAccountResult();
        assertThat(accountResult.isOk())
            .isTrue();

        final var account = accountResult.get();
        final var request = new TransactionAccountRequest(
            currency,
            new BigDecimal(amount)
        );

        final var response = accountState
            .getAccountClient()
            .deposit(account.id(), request);

        this.accountState.setAccountResult(response);
    }

    @Then("retiro de la cuenta {string} {string}")
    public void retiroDeLaCuenta(String amount, String currency) {
        final var accountResult = accountState
            .getAccountResult();
        assertThat(accountResult.isOk())
            .isTrue();

        final var account = accountResult.get();

        final var request = new TransactionAccountRequest(
            currency,
            new BigDecimal(amount)
        );

        final var result = accountState
            .getAccountClient()
            .withdraw(account.id(), request);
        accountState.setAccountResult(result);
    }

    @Then("el resultado debe ser error debido a {string}")
    public void elResultadoDebeSerErrorDebidoA(String mensajeError) {
        final var result = this.accountState.getAccountResult();
        assertThat(result.isError())
            .isTrue();
        final var apiError = result.getError();
        assertThat(apiError.detail())
            .isEqualTo(mensajeError);
    }
}
