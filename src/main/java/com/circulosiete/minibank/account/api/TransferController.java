package com.circulosiete.minibank.account.api;

import com.circulosiete.minibank.account.application.TransferService;
import com.circulosiete.minibank.account.domain.Money;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/transfers")
@NullMarked
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(
        @RequestBody TransferRequest request
    ) {
        IO.println("transfering");
        transferService.transfer(
            request.fromAccountId(),
            request.toAccountId(),
            new Money(request.currency(), request.amount())
        );

        final var response = new TransferResponse(
            request.fromAccountId(),
            request.toAccountId(),
            "OK"
        );

        return ResponseEntity.ok(response);
    }
}
