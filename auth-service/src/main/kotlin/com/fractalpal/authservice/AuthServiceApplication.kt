package com.fractalpal.authservice

import com.fractalpal.authservice.model.Account
import com.fractalpal.authservice.repository.AccountRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import org.springframework.stereotype.Component
import java.util.stream.Stream

@SpringBootApplication
@EnableResourceServer
@EnableAuthorizationServer
class AuthServiceApplication{

    @Bean
    fun jwtTokenEnhancer() : JwtAccessTokenConverter {
        val keyStoreKeyFactory = KeyStoreKeyFactory(ClassPathResource("jwt.jks"), "mySecretKey".toCharArray())
        val converter = JwtAccessTokenConverter()
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"))
        return converter
    }

    @Bean
    fun tokenStore() : TokenStore = JwtTokenStore(jwtTokenEnhancer())
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




