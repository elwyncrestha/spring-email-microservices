package com.fractalpal.emailservice

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
class EmailServiceApplication{

//    @Autowired
//    lateinit var environment: Environment
//
//    @Bean
//    fun init() =
//            CommandLineRunner {
////                Flux.fromArray(arrayOf("1","2","3","4"))
////                        .map { Integer.parseInt(it) }
////                        .filter { it % 2 == 0}
////                        .subscribe{ System.out.println("runner!!!!!!! $it")}
//            }

}

@RestController
@RefreshScope
class MessageRestController(@Value("\${message}") private val value: String){

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/message")
    fun read() : String{
        return value
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(EmailServiceApplication::class.java, *args)
}
