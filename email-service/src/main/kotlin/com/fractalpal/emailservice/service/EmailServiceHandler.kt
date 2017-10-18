package com.fractalpal.emailservice.service

import com.fractalpal.emailservice.model.ResponseMessage
import com.fractalpal.emailservice.model.SimpleEmail
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service(value = EmailService.HANDLER)
class EmailServiceHandler (@Qualifier(EmailService.MAIL_GUN) private val mailGunService: EmailService,
                           @Qualifier(EmailService.SEND_GRID) private val sendGridService: EmailService) : EmailService {

    override fun sendEmail(email: SimpleEmail): Mono<ResponseMessage> = sendGridService.sendEmail(email)
            .onErrorResume({
                mailGunService.sendEmail(email)
            })
            .onErrorReturn(ResponseMessage(ResponseMessage.CODE_ERROR, ResponseMessage.MSG_FAILED))
            .doOnError { System.out.println(it.message) }
}
