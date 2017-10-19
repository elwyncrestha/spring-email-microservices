package com.fractalpal.emailclient.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.util.FileCopyUtils
import java.io.IOException


@Configuration
class JwtConfiguration {

    @Autowired
    lateinit var jwtAccessTokenConverter: JwtAccessTokenConverter


    @Bean
    fun tokenStore(): TokenStore {
        return JwtTokenStore(jwtAccessTokenConverter)
    }

    @Bean
    protected fun jwtTokenEnhancer(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        val resource = ClassPathResource("public.cert")
        var publicKey: String? = null
        try {
            publicKey = String(FileCopyUtils.copyToByteArray(resource.inputStream))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        converter.setVerifierKey(publicKey)
        return converter
    }
}