package com.fractalpal.authservice.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration
import org.springframework.security.web.access.channel.ChannelProcessingFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@Order(-1)
@Configuration
@Import(AuthorizationServerEndpointsConfiguration::class)
class CorsEnabledAuthorizationServerSecurityConfiguration : AuthorizationServerSecurityConfiguration (){

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        val source = corsConfigurationSource()
        http.addFilterBefore(CorsFilter(source), ChannelProcessingFilter::class.java)
    }

    private fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("POST")
        config.addAllowedMethod("OPTIONS")
        //more config
        source.registerCorsConfiguration("/**", config)
        return source
    }
}