package com.fractalpal.authservice.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Account(val username: String, val password: String, val active: Boolean, @Id @GeneratedValue var id: Long = 0){
    @Suppress("unused")
    private constructor() : this("","", true) // JPA ;/
}