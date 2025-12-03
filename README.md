# minibank-bdd


```gherkin
Feature: Cuenta simple de Minibank
    Como cliente de MiniBank
    Yo quiero poder crear cuentas
    Para que puede realizar transacciones

    Scenario: Crear una nueva cuenta para "MXN"
        When creo una cuenta nueva para "MXN"
        Then la cuenta debe tener balance de "0.00" "MXN"

    Scenario: Crear una nueva cuenta y realizar un deposito
        When creo una cuenta nueva para "USD"
        And deposito a la cuenta "100.00" "USD"
        Then la cuenta debe tener balance de "100.00" "USD"

    Scenario: Crear una nueva cuenta y realizar un retiro
        When creo una cuenta nueva para "USD"
        And deposito a la cuenta "100.00" "USD"
        And retiro de la cuenta "50" "USD"
        Then la cuenta debe tener balance de "50.00" "USD"
        And retiro de la cuenta "60" "USD"
        Then el resultado debe ser error debido a "Insufficient balance"
```
