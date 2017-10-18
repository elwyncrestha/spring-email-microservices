package com.fractalpal.authservice.service

import com.fractalpal.authservice.repository.AccountRepository
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Normally we could use/have detail provider like LDAP, AD etc
 */
@Service
class AccountUserDetailsService(private val accountRepository: AccountRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails
            = accountRepository.findByUsername(username)
            .map { User(it.username,
                    it.password,
                    it.active,
                    it.active, // simplified
                    it.active, // simplified
                    it.active, // simplified
                    AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN") // simplified scoped
            )
            }
            .orElseThrow { UsernameNotFoundException("") }
}