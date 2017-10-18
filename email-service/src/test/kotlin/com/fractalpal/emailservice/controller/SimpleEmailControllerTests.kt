package com.fractalpal.emailservice.controller

import com.fractalpal.emailservice.model.ResponseMessage
import com.fractalpal.emailservice.model.SimpleEmail
import com.fractalpal.emailservice.service.EmailService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@WebFluxTest(EmailController::class)
internal class SimpleEmailControllerTests {

    @Autowired
    lateinit var webClient: WebTestClient

    @MockBean
    @Qualifier(EmailService.HANDLER)
    private lateinit var emailService: EmailService

    @Test
    fun `send email with body then status 200, CT JSON, SendResponse Success`() {

        val mockedEmail = SimpleEmail("me@email.com", "to@email.com", "subject", "message")
        given(emailService.sendEmail(mockedEmail)).willReturn(Mono.just(ResponseMessage()))

        this.webClient.post().uri("/email/send").accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(mockedEmail), SimpleEmail::class.java)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                // due to bug here we cannot test body -> expectBody (https://jira.spring.io/browse/SPR-15692; should be fixed in Kotlin 1.2)
                // TODO enable body testing in Kotlin 1.2
                .expectBody(ResponseMessage::class.java).isEqualTo<Nothing?>(ResponseMessage())
    }

    @Test
    fun `email send without body then status 4xx, empty body`() {

        this.webClient.post().uri("/email/send").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError
    }

}
