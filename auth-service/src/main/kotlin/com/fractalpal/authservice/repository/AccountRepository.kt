package com.fractalpal.authservice.repository

import com.fractalpal.authservice.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByUsername(username: String?) : Optional<Account>
}