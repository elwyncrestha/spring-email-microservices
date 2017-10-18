package com.fractalpal.emailservice

import com.fractalpal.emailservice.model.ResponseMessage
import com.fractalpal.emailservice.model.SimpleEmail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest
internal class SimpleEmailServiceIntegrationTests {

	lateinit var webClient: WebTestClient

	@Before
	fun before(){
        this.webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build() // test against live server
	}


	@Test
	fun `send email with body then status 200, CT JSON, SendResponse Success`() {

		val mockedEmail = SimpleEmail("me@email.com", "to@email.com", "subject", "message")

		this.webClient.post().uri("/email/send").accept(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(mockedEmail), SimpleEmail::class.java)
				.exchange()
				.expectStatus().isOk
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody(ResponseMessage::class.java).isEqualTo<Nothing?>(ResponseMessage())
	}

	@Test
	fun `send email without body then status 400, empty body`() {

		this.webClient.post().uri("/email/send").accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().is4xxClientError
	}

}
