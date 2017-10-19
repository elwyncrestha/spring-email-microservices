package com.fractalpal.emailclient.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fractalpal.emailclient.EmailSender
import com.fractalpal.emailclient.EmailWriter
import com.fractalpal.emailclient.model.GateWayResponse
import com.fractalpal.emailclient.model.ResponseMessage
import com.fractalpal.emailclient.model.SimpleEmail
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@RestController
@RequestMapping("/api/email")
/**
 * Email gateway client; architecture edge
 * Simplified with classes in single file etc - because it's demo
 */
class EmailApiGateway(private val emailSender: EmailSender,
                      private val emailWriter: EmailWriter,
                      private val objectMapper: ObjectMapper){

    // fallback method, we use messaging to persist message in the queue (we could use other strategies)
    fun fallback(email: SimpleEmail) : Mono<GateWayResponse> {
        emailWriter.write(objectMapper.writeValueAsString(email))
        return Mono.just(GateWayResponse(ResponseMessage(ResponseMessage.CODE_MESSAGE_IN_QUEUE, ResponseMessage.MSG_FALLBACK)))
    }

    @CrossOrigin
    @HystrixCommand(fallbackMethod = "fallback")
    @RequestMapping(value = "/send",
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE)
    )
    fun send(@RequestBody email: SimpleEmail) : Mono<GateWayResponse>
            = emailSender.send(email)
            .toMono()
            .map { GateWayResponse(it) }

    @RequestMapping(value = "/test", method = arrayOf(RequestMethod.GET))
    fun test() : String = "tested endpoint"
}