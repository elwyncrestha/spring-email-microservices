package com.fractalpal.authservice

import com.fractalpal.authservice.model.Account
import com.fractalpal.authservice.repository.AccountRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore
import org.springframework.stereotype.Component
import java.util.stream.Stream

@SpringBootApplication
@EnableResourceServer
class AuthServiceApplication{

    @Bean
    fun tokenStore(): TokenStore {
        return InMemoryTokenStore()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(AuthServiceApplication::class.java, *args)
}

@Component
class AccountsStubCLR(private val accountRepository: AccountRepository) : CommandLineRunner{
    override fun run(vararg args: String?) {
        System.out.println("## Creating faked user accounts ##")
        Stream.of("pbrzostowski,fractal", "user,password")
                .map { it.split(",") }
                .forEach({
                    tuple -> accountRepository.save(Account(username = tuple[0], password = tuple[1], active = true))
                })
    }
}





