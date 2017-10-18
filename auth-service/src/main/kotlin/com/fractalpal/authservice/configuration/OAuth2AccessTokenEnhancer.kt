package com.fractalpal.authservice.configuration

import com.fractalpal.authservice.model.Account
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import java.util.LinkedHashMap
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.token.TokenEnhancer


class OAuth2AccessTokenEnhancer : TokenEnhancer {

    override fun enhance(accessToken: OAuth2AccessToken, authentication: OAuth2Authentication): OAuth2AccessToken {

        val info = LinkedHashMap(
                accessToken.additionalInformation)

        // Add custom info to token.
        val user = authentication.principal as User
        info.put("username", user.username)

        (accessToken as DefaultOAuth2AccessToken).additionalInformation = info

        return accessToken
    }
}