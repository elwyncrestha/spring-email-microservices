package com.fractalpal.emailservice.controller

import com.fractalpal.emailservice.model.ResponseMessage
import com.fractalpal.emailservice.model.SimpleEmail
import com.fractalpal.emailservice.service.EmailService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/email")

class EmailController(@Qualifier(EmailService.HANDLER) private val emailService: EmailService){

    @CrossOrigin
    @PostMapping("/send", consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun send(@RequestBody email: SimpleEmail) : Mono<ResponseMessage> = emailService.sendEmail(email)

}