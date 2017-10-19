package com.fractalpal.emailclient

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fractalpal.emailclient.model.ResponseMessage
import com.fractalpal.emailclient.model.SimpleEmail
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.feign.EnableFeignClients
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.Output
import org.springframework.context.annotation.Bean
import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.IntegrationComponentScan
import org.springframework.integration.annotation.MessagingGateway
import org.springframework.messaging.MessageChannel
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.context.request.RequestContextListener


@IntegrationComponentScan
@EnableBinding(EmailChannel::class)
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
class EmailClientApplication{

    @Bean
    fun objectMapper() : ObjectMapper {
        val mapper = ObjectMapper()
        mapper.registerModule(KotlinModule())
        return mapper
    }

    @Bean
    fun requestContextListener(): RequestContextListener {
        return RequestContextListener()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(EmailClientApplication::class.java, *args)
}

interface EmailChannel{
    @Output
    fun output() : MessageChannel
}

@FeignClient("email-service") // uses load balanced email service
interface EmailSender {
    @RequestMapping(method = arrayOf(RequestMethod.POST), value = "/email/send")
    fun send(@RequestBody simpleEmail: SimpleEmail) : ResponseMessage
}

@MessagingGateway
interface EmailWriter {
    @Gateway(requestChannel = "output")
    fun write(emailJson: String)
}
