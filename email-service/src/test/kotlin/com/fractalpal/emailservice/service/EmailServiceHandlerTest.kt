package com.fractalpal.emailservice.service

import com.fractalpal.emailservice.model.ResponseMessage
import com.fractalpal.emailservice.model.SimpleEmail
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest
internal class EmailServiceHandlerTest{

    @MockBean
    @Qualifier(EmailService.SEND_GRID)
    private lateinit var sendGridService: EmailService

    @MockBean
    @Qualifier(EmailService.MAIL_GUN)
    private lateinit var mailGunService: EmailService

    @Autowired
    @Qualifier(EmailService.HANDLER)
    private lateinit var serviceHandler: EmailService

    private val mockedEmail = SimpleEmail("me@email.com", "to@email.com", "subject", "message")

    @Test
    fun `send email with send grid then success`() {

        BDDMockito.given(sendGridService.sendEmail(mockedEmail)).willReturn(Mono.just(ResponseMessage()))

        val result = serviceHandler.sendEmail(mockedEmail)

        StepVerifier.create(result)
                .expectNextMatches { ResponseMessage() == it }
                .verifyComplete()
    }

    @Test
    fun `send email with send grid then fail over to mail gun then success`() {

        BDDMockito.given(sendGridService.sendEmail(mockedEmail)).willReturn(Mono.error(RuntimeException(SendGridServiceHandler.ERROR_MSG)))
        BDDMockito.given(mailGunService.sendEmail(mockedEmail)).willReturn(Mono.just(ResponseMessage()))

        val result = serviceHandler.sendEmail(mockedEmail)

        StepVerifier.create(result)
                .expectNextMatches { ResponseMessage() == it }
                .verifyComplete()
    }

    @Test
    fun `send email with send grid then fail over to mail gun then no success`() {

        BDDMockito.given(sendGridService.sendEmail(mockedEmail)).willReturn(Mono.error(RuntimeException(SendGridServiceHandler.ERROR_MSG)))
        BDDMockito.given(mailGunService.sendEmail(mockedEmail)).willReturn(Mono.error(RuntimeException(SendGridServiceHandler.ERROR_MSG)))

        val result = serviceHandler.sendEmail(mockedEmail)

        StepVerifier.create(result)
                .expectNextMatches { ResponseMessage.errorMessage() == it }
                .verifyComplete()
    }

}