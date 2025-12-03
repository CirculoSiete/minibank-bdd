package com.circulosiete.minibank.account.api;

import com.circulosiete.minibank.TestcontainersConfiguration;
import com.circulosiete.minibank.account.application.AccountService;
import com.circulosiete.minibank.account.domain.Account;
import com.circulosiete.minibank.account.domain.Money;
import com.circulosiete.minibank.account.infrastructure.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.Generators;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
public class TransferControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void transferBetweenAccountsThroughController() throws Exception {
        // --- Setup inicial: crear cuentas ---
        // Arrange
        final var owner1 = Generators.timeBasedGenerator().generate();
        final var owner2 = Generators.timeBasedGenerator().generate();

        final var source = accountService.openAccount(
            owner1,
            "MXN"
        );
        accountService.deposit(
            source.getId(), Money.mxn(new BigDecimal("1000"))
        );
        final var target = accountService.openAccount(
            owner2,
            "MXN"
        );
        accountService.deposit(
            target.getId(), Money.mxn(new BigDecimal("500"))
        );

        // --- Crear request ---
        // Act
        final var request = new TransferRequest(
            source.getId(),
            target.getId(),
            "MXN",
            new BigDecimal("200")
        );

        String json = objectMapper.writeValueAsString(request);

        // --- Ejecutar la llamada ---
        mockMvc.perform(
                post("/v1/transfers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk());

        // --- Validar cambios ---
        // Assert
        Account updatedSource = accountRepository.findById(source.getId())
            .orElseThrow();

        Account updatedTarget = accountRepository.findById(target.getId())
            .orElseThrow();

        assertThat(updatedSource.getBalance().getAmount())
            .isEqualByComparingTo("800");

        assertThat(updatedTarget.getBalance().getAmount())
            .isEqualByComparingTo("700");
    }
}
