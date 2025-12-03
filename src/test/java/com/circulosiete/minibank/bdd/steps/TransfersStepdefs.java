package com.circulosiete.minibank.bdd.steps;

import com.circulosiete.minibank.account.api.dto.CreateAccountRequest;
import com.circulosiete.minibank.account.api.dto.TransactionAccountRequest;
import com.circulosiete.minibank.account.api.dto.TransferRequest;
import com.circulosiete.minibank.bdd.state.TransferState;
import com.fasterxml.uuid.Generators;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class TransfersStepdefs {
    @Autowired
    private TransferState transferState;

    @Given("Existen las siguientes cuentas:")
    public void existenLasSiguientesCuentas(DataTable table) {
        for (var row : table.asMaps()) {
            var name = row.get("name");
            var currency = row.get("currency");

            final var accountResult = transferState.getAccountClient().createAccount(
                new CreateAccountRequest(
                    Generators.timeBasedEpochGenerator().generate(),
                    currency
                )
            );

            transferState.getAccounts().put(name, accountResult);
        }
    }

    @And("deposito a la cuenta {string} la cantidad de {string} {string}")
    public void depositoALaCuentaLaCantidadDe(String accountId, String amount, String currency) {
        final var accountResult = transferState.findAccount(accountId);
        assertThat(accountResult.isPresent())
            .isTrue();

        final var account = accountResult.get();
        final var request = new TransactionAccountRequest(
            currency,
            new BigDecimal(amount)
        );

        final var response = transferState
            .getAccountClient()
            .deposit(account.id(), request);

        transferState
            .getAccounts()
            .put(accountId, response);
    }

    @Then("la cuenta {string} debe tener balance de {string} {string}")
    public void laCuentaDebeTenerBalanceDe(String accountId, String amount, String currency) {
        final var createdAccountResult = transferState.findAccount(accountId);
        assertThat(createdAccountResult.isPresent())
            .isTrue();

        final var account = createdAccountResult.get();

        assertThat(account)
            .as("Validar que la cuenta se creo con los datos esperados.")
            .isNotNull();

        assertThat(account.balance())
            .as("Validar que la cuenta tiene un balance inicial esperado.")
            .isEqualByComparingTo(new BigDecimal(amount));

        assertThat(account.currency())
            .as("Validar que la moneda de la cuenta es la esperada.")
            .isEqualTo(currency);
    }

    @When("Transfiero {string} {string} de la cuenta {string} a la cuenta {string}")
    public void transfieroDeLaCuentaALaCuenta(String amount, String currency, String sourceAccount, String destinationAccount) {
        final var source = transferState.getAccount(sourceAccount);
        final var destination = transferState.getAccount(destinationAccount);
        final var result = transferState.getTransferClient().transfer(
            new TransferRequest(
                source.id(),
                destination.id(),
                currency,
                new BigDecimal(amount)
            )
        );

        transferState.setTransferResult(result);
        transferState
            .getAccounts()
            .put(sourceAccount, transferState.getAccountClient().getAccount(source.id()));
        transferState
            .getAccounts()
            .put(destinationAccount, transferState.getAccountClient().getAccount(destination.id()));
    }

    @And("el resultado de la transferencia debe ser error debido a {string}")
    public void elResultadoDeLaTransferenciaDebeSerErrorDebidoA(String mensajeError) {
        final var transferResult = transferState.getTransferResult();
        assertThat(transferResult.isError())
            .isTrue();
        final var apiError = transferResult.getError();
        assertThat(apiError.detail())
            .isEqualTo(mensajeError);
    }
}
