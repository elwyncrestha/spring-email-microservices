package com.fractalpal.emailservice.service

import com.fractalpal.emailservice.model.ResponseMessage
import com.fractalpal.emailservice.model.SimpleEmail
import reactor.core.publisher.Mono

interface EmailService {
    fun sendEmail(email: SimpleEmail) : Mono<ResponseMessage>

    companion object {
        const val HANDLER: String = "handler" // overall handler that sends email using underlying services
        const val MAIL_GUN: String = "mail-gun"
        const val SEND_GRID: String = "send-grid"
    }
}