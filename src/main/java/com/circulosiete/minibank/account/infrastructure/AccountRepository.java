package com.circulosiete.minibank.account.infrastructure;

import com.circulosiete.minibank.account.domain.Account;
import com.circulosiete.minibank.account.domain.AccountStatus;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Page<Account> findByOwnerId(UUID ownerId, Pageable pageable);

    Page<Account> findByOwnerIdAndStatus(UUID ownerId, AccountStatus status, Pageable pageable);
}
