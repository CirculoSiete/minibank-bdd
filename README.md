# minibank-bdd

Este repositorio muestra como implementar BDD con Cucumber en una aplicación de Spring Boot.

Puedes ver la presentación completa [aquí](https://docs.google.com/presentation/d/1Y5qdIdzdrZi-DNvp2WAScNHdMmD8iGm681oxLMkPrzw/edit?usp=sharing).

## Requisitos

1. Java 25 (Requerido)
2. Gradle 9 (Opcional, ya que el proyecto usa Gradle Wrapper)


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
