package com.circulosiete.minibank.account.api;

import com.circulosiete.minibank.account.domain.Account;
import com.circulosiete.minibank.account.domain.Money;
import java.math.BigDecimal;

public class AccountMapper {

    public static AccountDto toDto(Account account) {
        Money balance = account.getBalance();
        return new AccountDto(
            account.getId(),
            account.getOwnerId(),
            account.getStatus(),
            balance.getCurrency(),
            balance.getAmount()
        );
    }

    // Si necesitas crear Account desde DTO (p.ej. en comandos):
    // (Normalmente preferirías usar el factory y no mapear directo DTO -> entidad)
    public static Money toMoney(String currency, BigDecimal amount) {
        return new Money(currency, amount);
    }
}
