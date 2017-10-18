package com.fractalpal.emailclient

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@WebFluxTest(EmailApiGateway::class)
internal class EmailApiGatewayTest{

    @Autowired
    lateinit var webClient: WebTestClient

    @MockBean
    private lateinit var emailSender: EmailSender

    @Test
    fun `send email with body then status 200 and success response`() {

        val mockedEmail = SimpleEmail("me@email.com", "to@email.com", "subject", "message")
        BDDMockito.given(emailSender.send(mockedEmail)).willReturn(ResponseMessage())

        this.webClient.post().uri("/email/send").accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(mockedEmail), SimpleEmail::class.java)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                // <Nothing?> due to bug here we cannot test body -> expectBody (https://jira.spring.io/browse/SPR-15692; should be fixed in Kotlin 1.2)
                .expectBody(GateWayResponse::class.java).isEqualTo<Nothing?>(GateWayResponse(ResponseMessage()))
    }

}

