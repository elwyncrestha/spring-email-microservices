package com.fractalpal.emailservice.service
import com.fractalpal.emailservice.model.ResponseMessage
import com.fractalpal.emailservice.model.SimpleEmail
import com.sendgrid.*
import org.apache.http.HttpStatus
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.io.IOException

@Component(value = EmailService.SEND_GRID)
class SendGridServiceHandler(private val env: Environment) : EmailService{

    override fun sendEmail(email: SimpleEmail): Mono<ResponseMessage> {
        val monoResponse = monoResponse(email)
        return monoResponse.map {
            if(it.statusCode != HttpStatus.SC_ACCEPTED){
                throw RuntimeException(ERROR_MSG) // exceptions is handled in EmailServiceHandler to allow fail over to next provider
            } else{
                return@map ResponseMessage()
            }
        }
    }

    private fun monoResponse(simpleEmail: SimpleEmail) : Mono<Response> = Mono.create<Response> {

        val from = Email(simpleEmail.from)
        val subject = simpleEmail.subject
        val to = Email(simpleEmail.to)
        val content = Content(CONTENT_TYPE, simpleEmail.text)
        val mail = Mail(from, subject, to, content)
        val apiKey = env[ENV_API_KEY]
        val sg = SendGrid(apiKey)
        val request = Request()

        try {
            request.method = Method.POST
            request.endpoint = ENDPOINT
            request.body = mail.build()
            val response = sg.api(request)
            it.success(response)
        } catch (ex: IOException) {
            it.error(ex)
        }
    }

    companion object {
        const val ENV_API_KEY = "SENDGRID_API_KEY"

        const val CONTENT_TYPE = "text/plain"
        const val ENDPOINT = "mail/send"

        const val ERROR_MSG = "send mail cannot deliver an email message"
    }
}