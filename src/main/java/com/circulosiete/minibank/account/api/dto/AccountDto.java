package com.circulosiete.minibank.account.api.dto;

import com.circulosiete.minibank.account.domain.AccountStatus;
import java.math.BigDecimal;
import java.util.UUID;

public class AccountDto {

    public UUID id;
    public UUID ownerId;
    public AccountStatus status;
    public String currency;
    public BigDecimal balance;

    public AccountDto() {
    }

    public AccountDto(UUID id, UUID ownerId, AccountStatus status, String currency, BigDecimal balance) {
        this.id = id;
        this.ownerId = ownerId;
        this.status = status;
        this.currency = currency;
        this.balance = balance;
    }
}
