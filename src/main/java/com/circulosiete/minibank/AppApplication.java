package com.circulosiete.minibank;

import com.circulosiete.minibank.account.domain.AccountFactory;
import com.circulosiete.minibank.account.infrastructure.AccountRepository;
import com.fasterxml.uuid.Generators;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(AccountFactory factory, AccountRepository repository) {
        return (args) -> {
            IO.println("Chido");
            repository.save(
                factory.openAccount(
                    Generators.timeBasedEpochGenerator().generate(),
                    "MXN"
                )
            );

            repository.findAll().forEach(IO::println);
        };
    }

}
