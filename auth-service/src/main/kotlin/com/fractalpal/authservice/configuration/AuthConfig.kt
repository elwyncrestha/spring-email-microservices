package com.fractalpal.authservice.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter




@Configuration
@EnableAuthorizationServer
class AuthConfig(private val tokenStore: TokenStore,
                 private val tokenEnhancer: JwtAccessTokenConverter,
                 private val authenticationManager: AuthenticationManager) : AuthorizationServerConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security.allowFormAuthenticationForClients()
    }

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
                .withClient("acme")
                .secret("acmesecret")
                .autoApprove(true)
                .authorizedGrantTypes("implicit","refresh_token", "password", "authorization_code")
                .scopes("email")
                .authorities(Scopes.EMAIL_READ, Scopes.EMAIL_WRITE)

    }

    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.tokenStore(tokenStore).tokenEnhancer(tokenEnhancer).authenticationManager(authenticationManager)
    }
}