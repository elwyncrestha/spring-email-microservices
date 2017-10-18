package com.fractalpal.authservice.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore

@Configuration
@EnableResourceServer
class ResourceServerConfig(private val tokenStore: TokenStore) : ResourceServerConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.tokenStore(tokenStore).resourceId(SERVER_RESOURCE_ID)
    }

    @Throws(Exception::class)
    fun configure(http: HttpSecurity) {
        // ... Not important at this stage
    }

    companion object {
        private val SERVER_RESOURCE_ID = "oauth2-server"
    }
}