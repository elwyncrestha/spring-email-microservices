package com.fractalpal.emailservice.service

import com.fractalpal.emailservice.model.ResponseMessage
import com.fractalpal.emailservice.model.SimpleEmail
import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter
import com.sun.jersey.core.util.MultivaluedMapImpl
import org.apache.http.HttpStatus
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import javax.ws.rs.core.MediaType


@Component(value = EmailService.MAIL_GUN)
class MailGunServiceHandler(private val env: Environment) : EmailService {
    override fun sendEmail(email: SimpleEmail): Mono<ResponseMessage> {
        return monoResponse(email).map {
            if(it.status != HttpStatus.SC_OK){
                throw RuntimeException(ERROR_MSG)
            } else {
                return@map ResponseMessage()
            }
        }
    }

    private fun monoResponse(simpleEmail: SimpleEmail) : Mono<ClientResponse> = Mono.create<ClientResponse> {
        val apiKey = env[ENV_API_KEY]
        val client = Client.create()
        client.addFilter(HTTPBasicAuthFilter(API, apiKey))
        val webResource = client.resource(WEB_RESOURCE)
        val formData = MultivaluedMapImpl()
        formData.add(FORM_FROM, simpleEmail.from)
        formData.add(FORM_TO, simpleEmail.to)
        formData.add(FORM_SUBJECT, simpleEmail.subject)
        formData.add(FORM_TEXT, simpleEmail.text)
        try{
            val resp = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse::class.java, formData)
            it.success(resp)
        } catch (ex: RuntimeException){
            it.error(ex)
        }
    }

    companion object {
        const val ENV_API_KEY = "MAILGUN_API_KEY"

        const val API = "api"
        const val FORM_FROM = "from"
        const val FORM_TO = "to"
        const val FORM_SUBJECT = "subject"
        const val FORM_TEXT = "text"
        const val WEB_RESOURCE = "https://api.mailgun.net/v3/sandbox4ff62a870b614e47871617ccfa4f1a71.mailgun.org/messages"

        const val ERROR_MSG = "mail gun cannot deliver an email message"
    }
}