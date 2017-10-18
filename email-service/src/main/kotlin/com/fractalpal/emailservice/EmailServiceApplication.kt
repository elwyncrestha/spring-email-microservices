package com.fractalpal.emailservice

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fractalpal.emailservice.message.EmailChannel
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@EnableBinding(EmailChannel::class)
@EnableDiscoveryClient
@SpringBootApplication
class EmailServiceApplication

@RestController
@RefreshScope
class MessageRestController(@Value("\${message}") private val value: String){

    @Bean
    fun objectMapper() : ObjectMapper {
        val mapper = ObjectMapper()
        mapper.registerModule(KotlinModule())
        return mapper
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/message")
    fun read() : String{
        return value
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(EmailServiceApplication::class.java, *args)
}
