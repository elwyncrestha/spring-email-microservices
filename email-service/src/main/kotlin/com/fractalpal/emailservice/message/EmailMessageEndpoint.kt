package com.fractalpal.emailservice.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fractalpal.emailservice.model.SimpleEmail
import com.fractalpal.emailservice.service.EmailService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.stream.annotation.Input
import org.springframework.integration.annotation.MessageEndpoint
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.SubscribableChannel

/**
 * used to resend emails published from messaging queue after recovery
 */
@MessageEndpoint
class EmailMessageEndpoint(@Qualifier(EmailService.HANDLER) private val emailService: EmailService,
                           private val objectMapper: ObjectMapper){

    @ServiceActivator(inputChannel = "input")
    fun onNewEmail(emailJson: String){
        val email = objectMapper.readValue(emailJson, SimpleEmail::class.java)
        emailService.sendEmail(email).subscribe()
    }
}

interface EmailChannel{

    @Input
    fun input() : SubscribableChannel
}