package com.circulosiete.minibank.account.application;

import com.circulosiete.minibank.account.api.CreateAccountRequest;
import com.circulosiete.minibank.account.domain.Account;
import com.circulosiete.minibank.account.infrastructure.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountApplicationService {

    private final AccountRepository repository;
    private final AccountService accountService;


}
