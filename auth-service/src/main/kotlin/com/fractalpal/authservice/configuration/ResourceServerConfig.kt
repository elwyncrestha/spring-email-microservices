package com.fractalpal.authservice.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableResourceServer
class ResourceServerConfig(private val tokenStore: TokenStore) : ResourceServerConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.tokenStore(tokenStore).resourceId(SERVER_RESOURCE_ID)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint({ request, response, authException -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED) })
            .and()
            .authorizeRequests()
            .antMatchers("/**").authenticated()
            .and()
            .httpBasic()
    }

    companion object {
        private val SERVER_RESOURCE_ID = "oauth2-server"
    }
}