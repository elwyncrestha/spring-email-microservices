package com.fractalpal.eurekaservice

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class EurekaServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(EurekaServiceApplication::class.java, *args)
}
