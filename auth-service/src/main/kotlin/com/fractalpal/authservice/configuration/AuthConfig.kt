package com.fractalpal.authservice.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.context.annotation.Primary
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter



@Configuration
@EnableAuthorizationServer
class AuthConfig(private val tokenStore: TokenStore,
                 private val authenticationManager: AuthenticationManager) : AuthorizationServerConfigurerAdapter() {


//    @Bean
//    fun defaultAccessTokenConverter(): DefaultAccessTokenConverter {
//        return DefaultAccessTokenConverter()
//    }
//
//    @Bean
//    fun accessTokenConverter(): JwtAccessTokenConverter {
//        val converter = JwtAccessTokenConverter()
//        converter.accessTokenConverter = defaultAccessTokenConverter()
//        converter.setSigningKey("mysecretkey")
//        return converter
//    }
//
//    @Bean
//    @Throws(Exception::class)
//    fun tokenStore(): TokenStore {
//        return JwtTokenStore(accessTokenConverter())
//    }
//
//    @Bean
//    fun tokenEnhancer(): TokenEnhancer {
//        return OAuth2AccessTokenEnhancer()
//    }
//
//    @Bean
//    fun tokenEnhancerChain(): TokenEnhancerChain {
//        val tokenEnhancerChain = TokenEnhancerChain()
//        tokenEnhancerChain.setTokenEnhancers(arrayListOf(tokenEnhancer(), accessTokenConverter()))
//        return tokenEnhancerChain
//    }
//
//    @Bean
//    @Primary
//    @Throws(Exception::class)
//    fun tokenServices(): DefaultTokenServices {
//        val defaultTokenServices = DefaultTokenServices()
//        defaultTokenServices.setTokenStore(tokenStore())
//        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain())
//        return defaultTokenServices
//    }

    @Throws(Exception::class)
    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security.allowFormAuthenticationForClients()
    }

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
                .withClient("acme")
                .secret("acmesecret")
                .authorizedGrantTypes("password") // token for password
                .scopes("openid")

    }

    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
//        endpoints.tokenStore(tokenStore())
//                .tokenServices(tokenServices())
//                .authenticationManager(authenticationManager)
//        endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore).approvalStoreDisabled()
        endpoints.authenticationManager(authenticationManager)
    }
}