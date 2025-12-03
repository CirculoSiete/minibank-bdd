Feature: Transferencias simples
    Como cliente de MiniBank
    Yo quiero poder realizar transacciones con otros usuarios
    Para poder enviar y recibir dinero

    Scenario: Transferencia exitosa entre dos cuentas MXN
        Given Existen las siguientes cuentas:
            | name        | currency |
            | source      | MXN      |
            | destination | MXN      |
        Then la cuenta "source" debe tener balance de "0.00" "MXN"
        And la cuenta "destination" debe tener balance de "0.00" "MXN"
        When deposito a la cuenta "source" la cantidad de "100.00" "MXN"
        Then la cuenta "source" debe tener balance de "100.00" "MXN"
        When Transfiero "50.00" "MXN" de la cuenta "source" a la cuenta "destination"
        Then la cuenta "source" debe tener balance de "50.00" "MXN"
        And la cuenta "destination" debe tener balance de "50.00" "MXN"
        When Transfiero "60.00" "MXN" de la cuenta "source" a la cuenta "destination"
        Then el resultado de la transferencia debe ser error debido a "Insufficient balance"
        And la cuenta "source" debe tener balance de "50.00" "MXN"
        And la cuenta "destination" debe tener balance de "50.00" "MXN"

    Scenario: Transferencia entre dos cuentas de diferente currency
        Given Existen las siguientes cuentas:
            | name        | currency |
            | source      | MXN      |
            | destination | USD      |
        Then la cuenta "source" debe tener balance de "0.00" "MXN"
        And la cuenta "destination" debe tener balance de "0.00" "USD"
        When deposito a la cuenta "source" la cantidad de "100.00" "MXN"
        Then la cuenta "source" debe tener balance de "100.00" "MXN"
        When Transfiero "50.00" "MXN" de la cuenta "source" a la cuenta "destination"
        Then el resultado de la transferencia debe ser error debido a "currency mismatch between accounts"
        And la cuenta "source" debe tener balance de "100.00" "MXN"
        And la cuenta "destination" debe tener balance de "0.00" "USD"

